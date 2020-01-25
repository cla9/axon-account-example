package com.cqrs.command.transfer;

public class JejuBankCancelTransferCommand extends AbstractCancelTransferCommand {
    @Override
    public String toString() {
        return "JejuBankCancelTransferCommand{" +
                "srcAccountID='" + srcAccountID + '\'' +
                ", dstAccountID='" + dstAccountID + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferID + '\'' +
                '}';
    }
}
