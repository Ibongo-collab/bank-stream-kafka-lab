package com.bankstream.fraudwatcher.application.port.in;

import com.bankstream.fraudwatcher.domain.model.AccountId;
import com.bankstream.fraudwatcher.domain.model.FraudAlert;
import java.util.List;

public interface ListAlertsUseCase {

    List<FraudAlert> listAll();

    List<FraudAlert> listByAccount(AccountId accountId);
}
