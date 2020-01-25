package com.cqrs.command.transfer;


public class SeoulBankTransferCommand extends AbstractTransferCommand {
    @Override
    public String toString() {
        return "SeoulBankTransferCommand{" +
                "srcAccountID='" + srcAccountID + '\'' +
                ", dstAccountID='" + dstAccountID + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferID + '\'' +
                '}';
    }
}
