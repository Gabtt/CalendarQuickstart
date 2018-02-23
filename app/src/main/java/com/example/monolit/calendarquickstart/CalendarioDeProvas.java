package com.example.monolit.calendarquickstart;

import android.widget.Toast;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.util.Arrays;

/**
 * Created by gabriel_batistell on 23/02/18.
 */

public class CalendarioDeProvas {
    String calendarioId = "primary";





    public void adicionaProva(Item item, MeuCalendario meuCalendario, MeuCalendario.OnEventCreated listener){
        meuCalendario.createEvent(converteProvaEmEvento(item), calendarioId , listener);

    }

    public void removeProva(Item item, MeuCalendario meuCalendario){
        // TODO: 23/02/18 Aqui seria bom se tivesse um event_id, pois é a unica coisa necessaria pra remover um evento.
        //Converter a prova em evento não serve pra nada aqui.

    }
    public void atualizaProva(Item item, MeuCalendario meuCalendario, MeuCalendario.OnEventUpdated onEventUpdated){
        // TODO: 23/02/18 preciso do eventId
        String eventIdToUpdate = "idbemloco";
        meuCalendario.updateEvent(converteProvaEmEvento(item), eventIdToUpdate, calendarioId, onEventUpdated);
    }

    public Event converteProvaEmEvento(Item item){

        final Event event = new Event()
                .setSummary(item.Nome)
                .setDescription(item.Detalhes);


        DateTime startDateTime = new DateTime(item.UnixMilli);
        event.setStart(new EventDateTime().setDateTime(startDateTime));


        //Adicionando uma hora depois.
        DateTime endDateTime = new DateTime(item.UnixMilli+3600000 );
        event.setEnd(new EventDateTime().setDateTime(endDateTime));


        return event;
    }

    public void salvaCredenciais(){}
}
