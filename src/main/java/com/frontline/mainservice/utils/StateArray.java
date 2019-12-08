package com.frontline.mainservice.utils;

public class StateArray {
    public State sendRequestToServer;
    public State establishTLSConnection;
    public State receivedServerResponse;
    public State sendCertToMSchain;
    public State receivedMSChainResponse;
    public State validateServerCertificate;

    public StateArray() {
        this.sendRequestToServer = new State(1, StateDescription.SEND_REQUEST_TO_SERVER, StateMessage.FAILED);
        this.establishTLSConnection = new State(2, StateDescription.ESTABLISH_TLS_CONNECTION, StateMessage.FAILED);
        this.receivedServerResponse = new State(3, StateDescription.RECEIVED_SEVER_RESPONSE, StateMessage.FAILED);
        this.sendCertToMSchain = new State(4, StateDescription.SEND_CERT_TO_MSCHAIN, StateMessage.FAILED);
        this.receivedMSChainResponse = new State(5, StateDescription.RECEIVED_MSCHAIN_RESPONSE, StateMessage.FAILED);
        this.validateServerCertificate = new State(6, StateDescription.VALIDATE_SERVER_CERTIFICATE, StateMessage.FAILED);
    }

}
