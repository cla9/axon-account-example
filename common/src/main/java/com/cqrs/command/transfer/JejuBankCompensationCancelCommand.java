package com.cqrs.command.transfer;

public class JejuBankCompensationCancelCommand extends AbstractCompensationCancelCommand {
    @Override
    public String toString() {
        return "JejuBankCompensationCancelCommand{" +
                "srcAccountID='" + srcAccountID + '\'' +
                ", dstAccountID='" + dstAccountID + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferID + '\'' +
                '}';
    }
}
