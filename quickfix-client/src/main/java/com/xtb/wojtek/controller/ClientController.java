package com.xtb.wojtek.controller;

import com.xtb.wojtek.core.FixClient;
import com.xtb.wojtek.fields.MyPriceField;
import com.xtb.wojtek.fields.MyStringField;
import com.xtb.wojtek.messages.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ListStatusRequest;
import quickfix.fix44.Logon;
import quickfix.fix44.OrderCancelRequest;
import quickfix.fix44.OrderStatusRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/order")
@Slf4j
public class ClientController {

    /**
     * The Fix client.
     */
    @Autowired
    private FixClient fixClient;

    /**
     * Logon boolean.
     *
     * @return the boolean
     * @throws SessionNotFound the session not found
     */
    @GetMapping("/logon")
    public Boolean logon() throws SessionNotFound {
        SessionID sessionID = fixClient.sessionIds().get(0);
//        Session session = Session.lookupSession(sessionID);
//        session.logon();

        Logon message = new Logon();
        message.setField(new EncryptMethod(EncryptMethod.NONE_OTHER));
        message.setField(new HeartBtInt(30));
        message.setField(new Username("Wojtek"));
        message.setField(new Password("querty123"));
        message.setField(new SendingTime(LocalDateTime.now()));
        message.setField(new RawData("1234"));
        message.setField(new RawDataLength(4));
        Session.sendToTarget(message, sessionID);
        return true;
    }

    /**
     * Logout boolean.
     *
     * @return the boolean
     */
    @GetMapping("/logout")
    public Boolean logout() {
        SessionID sessionID = fixClient.sessionIds().get(0);
        Session session = Session.lookupSession(sessionID);
        session.logout();
        return true;
    }

    /**
     * New order string.
     *
     * @return the string
     * @throws SessionNotFound the session not found
     */
    @GetMapping("/new")
    public Boolean newOrder() throws SessionNotFound {
        SessionID sessionID = fixClient.sessionIds().get(0);
        quickfix.fix44.NewOrderSingle newOrderSingle = new quickfix.fix44.NewOrderSingle(
                new ClOrdID("IT001"), new Side(Side.BUY),
                new TransactTime(LocalDateTime.now()), new OrdType(OrdType.MARKET));
        newOrderSingle.set(new OrderQty(0));
        newOrderSingle.set(new CashOrderQty(0));
        newOrderSingle.set(new Price(0));
        newOrderSingle.set(new Symbol("BTC"));
        newOrderSingle.set(new HandlInst('1'));
        newOrderSingle.set(new Currency("CNY"));
        newOrderSingle.set(new TimeInForce(TimeInForce.DAY));
        boolean result = Session.sendToTarget(newOrderSingle, sessionID);
        return result;
    }

    /**
     * Cancel order boolean.
     *
     * @return the boolean
     * @throws SessionNotFound the session not found
     */
    @GetMapping("/cancel")
    public Boolean cancelOrder() throws SessionNotFound {
        SessionID sessionID = fixClient.sessionIds().get(0);
        OrderCancelRequest request = new OrderCancelRequest();
        request.set(new ClOrdID(UUID.randomUUID().toString()));
        request.set(new OrderID("123"));
        request.set(new OrderQty(2D));
        request.set(new OrigClOrdID("XXX"));
        request.set(new Side(Side.BUY));
        request.set(new Symbol("BTC"));
        request.set(new TransactTime(LocalDateTime.now()));
        request.set(new Text("Cancel Order!"));
        boolean result = Session.sendToTarget(request, sessionID);
        return result;
    }

    /**
     * Query order state boolean.
     *
     * @return the boolean
     * @throws SessionNotFound the session not found
     */
    @GetMapping("/state")
    public Boolean queryOrderState() throws SessionNotFound {
        OrderStatusRequest request = new OrderStatusRequest();
        SessionID sessionID = fixClient.sessionIds().get(0);
        request.set(new OrderID("IT0002"));
        request.set(new ClOrdID("*"));
        request.set(new Symbol("*"));
        request.set(new Side(Side.BUY));
        boolean result = Session.sendToTarget(request, sessionID);
        return result;
    }

    /**
     * Query order list state boolean.
     *
     * @return the boolean
     * @throws SessionNotFound the session not found
     */
    @GetMapping("/list/state")
    public Boolean queryOrderListState() throws SessionNotFound {
        ListStatusRequest request = new ListStatusRequest();
        SessionID sessionID = fixClient.sessionIds().get(0);
        request.set(new ListID("IT001;IT002;IT003;IT004"));
        boolean result = Session.sendToTarget(request, sessionID);
        return result;
    }

    /**
     * Send my message boolean.
     *
     * @return the boolean
     * @throws SessionNotFound the session not found
     */
    @GetMapping("/myg")
    public Boolean sendMyMessage() throws SessionNotFound {
        MyMessage message = new MyMessage();
        message.set(new MyStringField("Hello Fix"));
        message.set(new MyPriceField(2.2));
        SessionID sessionID = fixClient.sessionIds().get(0);
        boolean result = Session.sendToTarget(message, sessionID);
        return result;
    }
}