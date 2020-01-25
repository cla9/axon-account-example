package com.cqrs.command.transfer;

public class SeoulBankCancelTransferCommand extends AbstractCancelTransferCommand {
    @Override
    public String toString() {
        return "SeoulBankCancelTransferCommand{" +
                "srcAccountID='" + srcAccountID + '\'' +
                ", dstAccountID='" + dstAccountID + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferID + '\'' +
                '}';
    }
}
