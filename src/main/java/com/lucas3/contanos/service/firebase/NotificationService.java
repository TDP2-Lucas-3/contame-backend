package com.lucas3.contanos.service.firebase;

import com.lucas3.contanos.entities.Incident;
import com.lucas3.contanos.entities.User;
import com.lucas3.contanos.model.firebase.PushNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private FCMService fcmService;

    public void enviarNotificacionPrueba(User user){
        try{
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle("PRUEBA");
            request.setMessage("YO LO QUE QUIERO QUE QUIERAN LO MISMO QUE TODOS QUEREMOS");
            request.setToken(user.getFCMToken());
            fcmService.sendMessageToToken(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendIncidentNotification(User user, Incident incident){
        try{
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle("Te escuchamos");
            request.setMessage("Recibimos tu reporte de " + incident.getCategory().getName().toLowerCase());
            request.setToken(user.getFCMToken());
            fcmService.sendMessageToToken(request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendVoteNotification(User voter, Incident incident){
        try{
            Map<String,String> data = new HashMap<>();
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle(incident.getUser().getProfile().getName()+" " + incident.getUser().getProfile().getSurename());
            request.setMessage(getMessageVote(voter,incident));
            request.setToken(incident.getUser().getFCMToken());
            data.put("photo", voter.getProfile().getPhoto());
            data.put("voterName", voter.getProfile().getName() + " " + voter.getProfile().getSurename());
            data.put("userName", incident.getUser().getProfile().getName());
            data.put("incident", incident.getTitle());
            data.put("id", incident.getId().toString());
            fcmService.sendMessageToToken(data,request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getMessageVote(User voter, Incident incident){
        String voterName = voter.getProfile().getName() + " " + voter.getProfile().getSurename();
        String incidentTitle = incident.getTitle();
        String msg = voterName + " indicó que le gusta tu incidencia " + incidentTitle;
        return msg;
    }

    public void sendChangeStateNotification(Incident incident){
        try{
            Map<String,String> data = new HashMap<>();
            PushNotificationRequest request = new PushNotificationRequest();
            request.setTitle(incident.getTitle());
            request.setMessage(getChangeStateMessage(incident));
            request.setToken(incident.getUser().getFCMToken());
            data.put("incident", incident.getTitle());
            data.put("state", incident.getState().toString());
            fcmService.sendMessageToToken(data,request);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String getChangeStateMessage(Incident incident){
        String incidentTitle = incident.getTitle();
        String msg = "Tu incidencia "+ incidentTitle +" cambio de estado a " + incident.getState().toString();
        return msg;
    }
}
