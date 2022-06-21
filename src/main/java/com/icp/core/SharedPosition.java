package com.icp.core;

import static org.ic4j.candid.types.Type.*;
import static org.ic4j.candid.types.Type.BOOL;
import static org.ic4j.candid.types.Type.NAT;
import static org.ic4j.candid.types.Type.PRINCIPAL;

import java.math.BigInteger;
import org.ic4j.candid.annotations.Field;
import org.ic4j.types.Principal;

public class SharedPosition {
    @Field(NAT)
    private BigInteger id;
    @Field(PRINCIPAL)
    public Principal owner;
    @Field(NAT)
    private BigInteger collateralAmount;
    @Field(NAT)
    private BigInteger stableAmount;
    @Field(BOOL)
    private Boolean deleted;

    public SharedPosition() {
    }

    public SharedPosition(BigInteger id, BigInteger collateralAmount, BigInteger stableAmount) {
        this.id = id;
        this.collateralAmount = collateralAmount;
        this.stableAmount = stableAmount;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getCollateralAmount() {
        return collateralAmount;
    }

    public void setCollateralAmount(BigInteger collateralAmount) {
        this.collateralAmount = collateralAmount;
    }

    public BigInteger getStableAmount() {
        return stableAmount;
    }

    public void setStableAmount(BigInteger stableAmount) {
        this.stableAmount = stableAmount;
    }

    public Principal getOwner() {
        return owner;
    }

    public void setOwner(Principal owner) {
        this.owner = owner;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "SharedPosition{" +
                "id=" + id +
                ", owner=" + owner +
                ", collateralAmount=" + collateralAmount +
                ", stableAmount=" + stableAmount +
                ", deleted=" + deleted +
                '}';
    }
}
