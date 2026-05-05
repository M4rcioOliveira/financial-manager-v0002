package com.github.m4rcioliveira.financial_manager_v0002.handler;

import com.github.m4rcioliveira.financial_manager_v0002.model.Historico;

public interface IMessageHandler {

    void execute(Historico historico);

}
