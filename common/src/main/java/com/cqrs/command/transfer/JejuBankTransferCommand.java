package com.cqrs.command.transfer;

public class JejuBankTransferCommand extends AbstractTransferCommand {
    @Override
    public String toString() {
        return "JejuBankTransferCommand{" +
                "srcAccountID='" + srcAccountID + '\'' +
                ", dstAccountID='" + dstAccountID + '\'' +
                ", amount=" + amount +
                ", transferID='" + transferID + '\'' +
                '}';
    }
}
