package com.example.monolit.calendarquickstart.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

/**
 * Created by gabriel_batistell on 23/02/18.
 */

public class CalendarioDeProvas {
    String calendarioId = "primary";





    public void adicionaProva(ItemProva itemProva, MeuCalendario meuCalendario, MeuCalendario.OnEventCreated listener){
        meuCalendario.createEvent(converteProvaEmEvento(itemProva), getCalendarioId() , listener);

    }

    public void removeProva(ItemProva itemProva, MeuCalendario meuCalendario){
        meuCalendario.deleteEvent(getEventIdNaTabelaDeIds(itemProva), getCalendarioId(), new MeuCalendario.OnEventDeleted() {
            @Override
            public void onDeleted(Void executou) {

            }
        });

    }

    public void atualizaProva(ItemProva itemProva, MeuCalendario meuCalendario, MeuCalendario.OnEventUpdated onEventUpdated){
        meuCalendario.updateEvent(converteProvaEmEvento(itemProva), getCalendarioId(), onEventUpdated);
    }

    public Event converteProvaEmEvento(ItemProva itemProva){

        final Event event = new Event()
                .setSummary(itemProva.Nome)
                .setDescription(itemProva.Detalhes)
                .setId(getEventIdNaTabelaDeIds(itemProva));


        DateTime startDateTime = new DateTime(itemProva.UnixMilli);
        event.setStart(new EventDateTime().setDateTime(startDateTime));


        //Adicionando uma hora depois.
        // TODO: 06/03/2018 hora final do evento
        DateTime endDateTime = new DateTime(itemProva.UnixMilli+3600000 );
        event.setEnd(new EventDateTime().setDateTime(endDateTime));


        return event;
    }

    String getEventIdNaTabelaDeIds(ItemProva itemProva){
        // TODO: 06/03/2018 Pegar o id da prova relacionado ao id do evento.
        return "id";
    }
    public String getCalendarioId() {
        return calendarioId;
    }

    public void salvaCredenciais(){}
}
