package com.lucas3.contanos.service;

import com.lucas3.contanos.entities.*;
import com.lucas3.contanos.model.exception.FailedReverseGeocodeException;
import com.lucas3.contanos.model.filters.DataFilter;
import com.lucas3.contanos.model.response.CategoryData;
import com.lucas3.contanos.model.response.StateData;
import com.lucas3.contanos.model.response.StateDataResponse;
import com.lucas3.contanos.model.response.geocoding.LocationResponse;
import com.lucas3.contanos.repository.IncidentRepository;
import com.lucas3.contanos.repository.ProfileRepository;
import com.lucas3.contanos.repository.UserRepository;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class DataService implements IDataService{

    private static final Random RANDOM = new Random();

    private static final double MIN_LAT= -34.6560;
    private static final double MAX_LAT= -34.5437;

    private static final double MIN_LON= -58.5213;
    private static final double MAX_LON= -58.3749;

    private static final int CANT_INCIDENTS = 700;

    @Autowired
    private IGeocodingService geocodingService;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private static final Map<EIncidentCategory, List<String>> subcategories;
    static {
        Map<EIncidentCategory, List<String>> map = new HashMap<>();
        map.put(EIncidentCategory.ALUMBRADO, Arrays.asList("Cable suelto",
                "Despeje de ramas",
                "Luminaria no funciona",
                "Luminaria prendida de día",
                "Pedido de luminaria nueva",
                "Poste en mal estado",
                "Reclamos varios"));

        map.put(EIncidentCategory.AUTOS, Arrays.asList("Autos abandonados"));

        map.put(EIncidentCategory.VIA_PUBLICA, Arrays.asList("Corte de pasto",
                "Bache",
                "Cordón a reparar",
                "Semáforo en riesgo de caída",
                "Solicitud de rampa para personas con movilidad reducida",
                "Solicitudes varias"));

        map.put(EIncidentCategory.LIMPIEZA, Arrays.asList("Destapar desagüe",
                "Falta servicio de recolección",
                "Falta servicio de barrido",
                "Solicitud de recolección",
                "Falta tapa boca de desagüe",
                "Reparar desagüe","Reclamos varios"));

        map.put(EIncidentCategory.ESPACIOS_VERDES, Arrays.asList("Corte de césped",
                "Extracción de Árbol",
                "Limpieza y vaciado de cestos de espacios públicos",
                "Solicitud de poda",
                "Retiro de poda",
                "Despeje de ramas en luminaria/semáforo",
                "Permiso de extracción a cargo del vecino"));

        map.put(EIncidentCategory.USO_ESPACIO, Arrays.asList("Carteles Publicitarios con riesgo de caída",
                "Carteles o columnas abandonadas",
                "Ocupación indebida del espacio público",
                "Puesto abandonado o falta mantenimiento",
                "Reparación de rampa para personas con movilidad reducida"));

        subcategories = Collections.unmodifiableMap(map);
    }




    @Override
    public StateDataResponse getStatesData(DataFilter filter) throws ParseException {
        StateDataResponse response = new StateDataResponse();
        List<CategoryData> totals = new ArrayList<>();

        for (EIncidentCategory category: EIncidentCategory.values()) {
            CategoryData caData = new CategoryData();
            caData.setCategory(category.getValue());
            caData.setValue(incidentRepository.countByCategory(filter,category));
            totals.add(caData);
        }
        totals.sort(Comparator.comparingInt(CategoryData::getValue).reversed());
        response.setCategoryTotals(totals);

        List<StateData> data = new ArrayList<>();

        for (EIncidentStatePublic state: EIncidentStatePublic.values()) {
            StateData stateData = new StateData();
            stateData.setName(state.getValue());
            List<CategoryData> categoryData = new ArrayList<>();
            for (CategoryData category: totals) {
                CategoryData caData = new CategoryData();
                EIncidentCategory cat = EIncidentCategory.get(category.getCategory());
                caData.setCategory(cat.getValue());
                caData.setValue(incidentRepository.countByStateAndCategory(filter,state,cat));
                categoryData.add(caData);
            }
            stateData.setCategories(categoryData);
            data.add(stateData);
        }
        response.setData(data);


        return response;

    }

    @Override
    public List<Incident> getIncidents(String category) {
        if(category != null && !category.isEmpty()){
            EIncidentCategory cat = EIncidentCategory.get(category);
            return incidentRepository.findAll(cat);
        }
        return incidentRepository.findAll();
    }

    @Async
    @Override
    public void loadData() {
        try {
            int sizeIncidents = incidentRepository.findAll().size();
            User user = getUser();
            for (int i = 0; i <CANT_INCIDENTS ; i++) {
                createRandomIncident(i+sizeIncidents, user);
                Thread.sleep(1000);
            }
            System.out.println("TERMINE EL TRABAJO");
        } catch (InterruptedException | FailedReverseGeocodeException e) {
            e.printStackTrace();
        }
    }

    private User getUser(){
        Optional<User> user = userRepository.findByEmail("bot@contame.com");
        return user.orElseGet(this::createUser);
    }
    private User createUser(){
        User user = new User("bot@contame.com",
                encoder.encode("botContame123"));
        user.setUserState(EUserState.ACTIVO);
        userRepository.save(user);

        Profile profile = new Profile("Juan", "Contame");
        profileRepository.save(profile);
        user.setProfile(profile);
        user.setRol(ERole.ROLE_USER);

        return user;

    }

    private void createRandomIncident(Integer number, User user) throws FailedReverseGeocodeException{
        List<EIncidentCategory> categories = Arrays.asList(EIncidentCategory.values());
        EIncidentCategory category = categories.get(RANDOM.nextInt(categories.size()));

        List<String> types = subcategories.get(category);
        String type = types.get(RANDOM.nextInt(types.size()));

        List<EIncidentStatePublic> publicStates = Arrays.asList(EIncidentStatePublic.values());
        EIncidentStatePublic statePublic = publicStates.get(RANDOM.nextInt(categories.size()));

        List<EIncidentStatePrivate> privateStates = Arrays.asList(EIncidentStatePrivate.values());
        EIncidentStatePrivate statePrivate = privateStates.get(RANDOM.nextInt(privateStates.size()));

        double randomLat = MIN_LAT + RANDOM.nextDouble() * (MAX_LAT - MIN_LAT);
        double randomLon = MIN_LON + RANDOM.nextDouble() * (MAX_LON - MIN_LON);

        Incident incident = new Incident("Incidente numero " + number.toString(),category,"Este incidente esta generado automaticamente", randomLat, randomLon);

        LocationResponse location = geocodingService.getLocationFromCoordinates(randomLat, randomLon);
        if(location.getHood() != null){
            incident.setLocation(location.getAddress());
            incident.setHood(location.getHood());

            incident.setUser(user);
            incident.setState(statePublic);
            incident.setStatePrivate(statePrivate);
            incident.setSubcategory(type);

            LocalDate d1 = new LocalDate(2020,1,1);
            LocalDate d2 = new LocalDate(2020,12,31);
            int days = Days.daysBetween(d1, d2).getDays();
            LocalDate randomDate = d1.plusDays(RANDOM.nextInt(days+1));
            incident.setCreationDate(randomDate.toDate());

            incidentRepository.save(incident);

        }


    }
}
