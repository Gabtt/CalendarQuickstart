package com.example.monolit.calendarquickstart.calendar;

import android.app.Activity;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel_batistell on 23/02/18.
 */

public class CalendarioDeProvas {


    private static List<Event> eventList = new ArrayList<>();
    private static List<ItemProva> itemProvaList = new ArrayList<>();
    CalendarApi meuCalendario;


    public CalendarioDeProvas(CalendarApi calendario){
        this.meuCalendario = calendario;
    }


    public void adicionaProva(ItemProva itemProva, CalendarApi.OnEventCreated listener){
        meuCalendario.createEvent(converteProvaEmEvento(itemProva), getCalendarioId(), listener);

    }
    public void adicionaProva(Event event, CalendarApi.OnEventCreated listener){
        meuCalendario.createEvent(event, getCalendarioId(), listener);

    }

   public void removeProva(ItemProva itemProva){
       meuCalendario.deleteEvent(getEventIdNaTabelaDeIds(itemProva),  getCalendarioId(), new CalendarApi.OnEventDeleted() {
           @Override
           public void onDeleted(Void executou) {

           }
       });

   }

   public void atualizaProva(ItemProva itemProva, CalendarApi.OnEventUpdated onEventUpdated){
       meuCalendario.updateEvent(converteProvaEmEvento(itemProva), getCalendarioId(), onEventUpdated);
   }

   Event converteProvaEmEvento(ItemProva itemProva){

        final Event event = new Event()
                .setSummary(itemProva.Nome)
                .setDescription(itemProva.Detalhes)
                .setId(getEventIdNaTabelaDeIds(itemProva));


        DateTime startDateTime = new DateTime(itemProva.UnixMilli);
        event.setStart(new EventDateTime().setDateTime(startDateTime));


        //Adicionando uma hora depois.
        // TODO: 06/03/2018 hora final do evento
        DateTime endDateTime = new DateTime(itemProva.UnixMilli+360000 );
        event.setEnd(new EventDateTime().setDateTime(endDateTime));


        return event;
    }

    public void adicionaProvasJaAceitasNoCalendario(){
        getListaDeProvas();
        for (ItemProva itemProva : itemProvaList) {
            adicionaProva(itemProva, new CalendarApi.OnEventCreated() {
                @Override
                public void onCreated(Event event) {

                }
            });
        }

    }

    public void getListaDeProvas(){
        // itemProvaList = AvaliaçoesHelper.getAvaliacoesFuturasOuPossivelmenteFuturas(tipo)
    }

    public String getEventIdNaTabelaDeIds(ItemProva itemProva){
        // TODO: 06/03/2018 Pegar o id da prova relacionado ao id do evento.
        return "id";
    }
    public String getCalendarioId() {
        return "primary";
    }



}
