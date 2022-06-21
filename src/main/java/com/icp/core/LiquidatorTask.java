package com.icp.core;

import static java.lang.String.format;
import static java.math.BigInteger.valueOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LiquidatorTask{

    private final LiquidatorService liquidatorService;

    @Scheduled(cron = "*/10 * * * * *")
    public void run() {
        System.out.println("Task Run");
            final BigDecimal price = liquidatorService.getPrice();
            final BigInteger lastPositionId = liquidatorService.getLastPositionId();
            final BigInteger limit = valueOf(100);
            for (int offset = 0; offset < lastPositionId.intValue(); offset += 100) {
                List<SharedPosition> liquidatedPositions = liquidatorService.getPositions(limit, valueOf(offset))
                        .stream()
                        .filter(sharedPosition -> !sharedPosition.getDeleted())
                        .filter(positionDTO -> liquidatorService.isLiquidated(positionDTO, price))
                        .collect(Collectors.toList());
                liquidatedPositions.forEach(liquidatedPosition -> {
                    System.out.println(format("Start to liquidate %s", liquidatedPosition));
                    try {
                        liquidatorService.liquidate(liquidatedPosition.getId());
                    } catch (Exception e) {
                        System.out.println(format("Can't liquidate %s", e.getMessage()));
                    }
                    System.out.println(format("Finish to liquidate %s", liquidatedPosition));
                });
                System.out.println(format("Task Finish, liquidatedPositions count %s", liquidatedPositions.size()));
            }

    }
}
