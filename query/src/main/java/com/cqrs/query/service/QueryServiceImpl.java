package com.cqrs.query.service;

import com.cqrs.query.entity.HolderAccountSummary;
import com.cqrs.query.loan.LoanLimitQuery;
import com.cqrs.query.loan.LoanLimitResult;
import com.cqrs.query.query.AccountQuery;
import com.cqrs.query.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class QueryServiceImpl implements QueryService {
    private final Configuration configuration;
    private final QueryGateway queryGateway;
    private final AccountRepository repository;

    @Override
    public void reset() {
        configuration.eventProcessingConfiguration()
                .eventProcessorByProcessingGroup("accounts",
                        TrackingEventProcessor.class)
                .ifPresent(trackingEventProcessor -> {
                    trackingEventProcessor.shutDown();
                    trackingEventProcessor.resetTokens(); // (1)
                    trackingEventProcessor.start();
                });
    }

    @Override
    public HolderAccountSummary getAccountInfo(String holderId) {
        AccountQuery accountQuery = new AccountQuery(holderId);
        log.debug("handling {}", accountQuery);
        return queryGateway.query(accountQuery, ResponseTypes.instanceOf(HolderAccountSummary.class)).join();
    }

    @Override
    public Flux<HolderAccountSummary> getAccountInfoSubscription(String holderId) {
        AccountQuery accountQuery = new AccountQuery(holderId);
        log.debug("handling {}", accountQuery);

        SubscriptionQueryResult<HolderAccountSummary, HolderAccountSummary> queryResult = queryGateway.subscriptionQuery(accountQuery,
                ResponseTypes.instanceOf(HolderAccountSummary.class),
                ResponseTypes.instanceOf(HolderAccountSummary.class)
        );

        return Flux.create(emitter -> {
            queryResult.initialResult().subscribe(emitter::next);
            queryResult.updates()
                    .doOnNext(holder -> {
                        log.debug("doOnNext : {}, isCanceled {}", holder, emitter.isCancelled());
                        if (emitter.isCancelled()) {
                            queryResult.close();
                        }
                    })
                    .doOnComplete(emitter::complete)
                    .subscribe(emitter::next);
        });
    }

    @Override
    public List<LoanLimitResult> getAccountInfoScatterGather(String holderId) {
        HolderAccountSummary accountSummary = repository.findByHolderId(holderId).orElseThrow();

        return queryGateway.scatterGather(new LoanLimitQuery(accountSummary.getHolderId(), accountSummary.getTotalBalance()),
                ResponseTypes.instanceOf(LoanLimitResult.class),
                30, TimeUnit.SECONDS)
                .collect(Collectors.toList());
    }
}
