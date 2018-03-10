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


    public void adicionaProva(final ItemProva itemProva, final CalendarApi.OnEventCreated listener){
        meuCalendario.createEvent(converteProvaEmEvento(itemProva), getCalendarioId(),  new CalendarApi.OnEventCreated() {
            @Override
            public void onCreated(Event event) {
                salvaEventIdERelacionaComProvaId(event.getId(),itemProva.Avaliacao_ID);
                listener.onCreated(event);

            }
        });
    }

    @Deprecated
    public void adicionaProva(Event event, CalendarApi.OnEventCreated listener){
        meuCalendario.createEvent(event, getCalendarioId(), listener);

    }



    @Deprecated
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

   public Event converteProvaEmEvento(ItemProva itemProva){

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

    /**
     *
     * @param eventId
     * @param AvaliacaoId Usar Avaliacao.AvaliavaoId
     */
    public void salvaEventIdERelacionaComProvaId(String eventId, int AvaliacaoId){



        // TODO: 10/03/18 salvar id de prova e de evento na tabela relacional de provas e eventos.
        // TODO: 10/03/18 O id só vai ser salvo depois de ter adiciconado evento no calendar.
    }

    @Deprecated
    public void deletaEventIdEProvaIdDaTabela(String eventId, int AvaliacaoId){
        // TODO: 10/03/18 deleta relação de id de prova com id de evento na tebela pois evento foi REMOVIDO. Creio que nunca vai ser usado, apenas sera feito adição e edição.
    }

    @Deprecated
    public void deletaEventIdEProvaIdDaTabelaComBaseNoEventId(String eventId){}

    @Deprecated
    public void deletaEventIdEProvaIdDaTabelaComBaseNaProvaId(int AvaliacaoId){}

    public String getEventIdNaTabelaDeIds(ItemProva itemProva){
        // TODO: 06/03/2018 Pegar o id da prova relacionado ao id do evento.
        return "id";
    }
    public String getCalendarioId() {
        return "primary";
    }



}
