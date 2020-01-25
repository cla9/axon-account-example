package com.cqrs.query.version;

import com.cqrs.event.HolderCreationEvent;
import org.axonframework.serialization.SimpleSerializedType;
import org.axonframework.serialization.upcasting.event.IntermediateEventRepresentation;
import org.axonframework.serialization.upcasting.event.SingleEventUpcaster;

public class HolderCreationEventV1 extends SingleEventUpcaster {
    private static SimpleSerializedType targetType = new SimpleSerializedType(HolderCreationEvent.class.getTypeName(), null);

    @Override
    protected boolean canUpcast(IntermediateEventRepresentation intermediateRepresentation) {
        return intermediateRepresentation.getType().equals(targetType);
    }

    @Override
    protected IntermediateEventRepresentation doUpcast(IntermediateEventRepresentation intermediateRepresentation) {
        return intermediateRepresentation.upcastPayload(
                new SimpleSerializedType(targetType.getName(), "1.0"),
                org.dom4j.Document.class,
                document -> {
                    document.getRootElement()
                            .addElement("company")
                            .setText("N/A");
                    return document;
                }
        );
    }
}
