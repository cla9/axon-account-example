package com.cqrs.command.transfer;


public class SeoulBankCompensationCancelCommand extends AbstractCompensationCancelCommand {
    @Override
    public String toString() {
        return "SeoulBankCompensationCancelCommand{" +
                "srcAccountID='" + srcAccountID + '\'' +
                ", dstAccountID='" + dstAccountID + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferID + '\'' +
                '}';
    }
}
