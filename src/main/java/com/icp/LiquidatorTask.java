package com.icp;

import static java.lang.String.format;
import static java.math.BigInteger.valueOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class LiquidatorTask extends TimerTask {

    private final LiquidatorService liquidatorService = new LiquidatorService();

    @Override
    public void run() {
        System.out.println("Task Run");
        try {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
