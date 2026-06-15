package com.oms.app.orders.domain.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.InvalidValueException;
import com.oms.app.common.exception.ResourceNotFoundException;
import com.oms.app.orders.domain.vo.OrderId;

public final class OrderDomainExceptions {

    // Business
    private static final ExceptionCode ORDER_ITEMS_REQUIRED = ExceptionCode.of("orders.items_required");
    private static final ExceptionCode STATUS_TRANSITION_NOT_ALLOWED = ExceptionCode.of("orders.status_transition_not_allowed");
    private static final ExceptionCode QUANTITY_MUST_BE_POSITIVE = ExceptionCode.of("orders.quantity_must_be_positive", "quantity");

    // VO's
    private static final ExceptionCode ORDER_ID_REQUIRED = ExceptionCode.of("orders.order_id.required", "orderId");
    private static final ExceptionCode ORDER_ITEM_ID_REQUIRED = ExceptionCode.of("orders.order_item_id.required", "orderItemId");
    private static final ExceptionCode ORDER_NOTES_INVALID = ExceptionCode.of("orders.order_notes.invalid", "notes");
    private static final ExceptionCode ORDER_ITEM_NOTES_INVALID = ExceptionCode.of("orders.order_item_notes.invalid", "orderItemNotes");

    private OrderDomainExceptions() {}

    /** Business **/
    public static BusinessConflictException mustContainAtLeastOneItem() {
        return new BusinessConflictException(ORDER_ITEMS_REQUIRED);
    }

    public static BusinessConflictException statusTransitionNotAllowed(OrderId id, Object from, Object to) {
        return new BusinessConflictException(STATUS_TRANSITION_NOT_ALLOWED, id.value(), from, to);
    }

    public static InvalidValueException quantityMustBePositive(int value) {
        return new InvalidValueException(QUANTITY_MUST_BE_POSITIVE, value);
    }

    /** VO's **/
    public static InvalidValueException orderIdRequired() {
        return new InvalidValueException(ORDER_ID_REQUIRED);
    }

    public static InvalidValueException orderItemIdRequired() {
        return new InvalidValueException(ORDER_ITEM_ID_REQUIRED);
    }

    public static InvalidValueException invalidOrderNotes(int maxLength) {
        return new InvalidValueException(ORDER_NOTES_INVALID, maxLength);
    }

    public static InvalidValueException invalidOrderItemNotes(int maxLength) {
        return new InvalidValueException(ORDER_ITEM_NOTES_INVALID, maxLength);
    }

    public static ResourceNotFoundException orderNotFound(OrderId id) {
        return new ResourceNotFoundException(
                ExceptionCode.of("orders.not_found", "orderId"),
                id.value()
        );
    }
}