package com.oms.app.tables.domain.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.InvalidValueException;
import com.oms.app.tables.domain.TableStatus;

import java.util.Set;
import java.util.stream.Collectors;

public final class TableDomainExceptions {

    // Business
    private static final ExceptionCode STATUS_CHANGE_NOT_ALLOWED     = ExceptionCode.of("tables.status.change_not_allowed");
    private static final ExceptionCode TABLE_CAPACITY_CHANGE_NOT_ALLOWED_IN_STATUS  = ExceptionCode.of("tables.capacity.change_not_allowed_in_status");

    // VO
    private static final ExceptionCode TABLE_CAPACITY_INVALID       = ExceptionCode.of("tables.capacity.invalid",        "capacity");
    private static final ExceptionCode TABLE_CODE_BLANK             = ExceptionCode.of("tables.code.blank",              "code");
    private static final ExceptionCode TABLE_CODE_TOO_LONG          = ExceptionCode.of("tables.code.too_long",           "code");
    private static final ExceptionCode TABLE_CODE_INVALID_FORMAT    = ExceptionCode.of("tables.code.invalid_format",     "code");

    // Placement VO
    private static final ExceptionCode TABLE_PLACEMENT_X_INVALID = ExceptionCode.of("tables.placement.x.invalid", "x");
    private static final ExceptionCode TABLE_PLACEMENT_Y_INVALID = ExceptionCode.of("tables.placement.y.invalid", "y");
    private static final ExceptionCode TABLE_PLACEMENT_WIDTH_INVALID = ExceptionCode.of("tables.placement.width.invalid", "width");
    private static final ExceptionCode TABLE_PLACEMENT_HEIGHT_INVALID = ExceptionCode.of("tables.placement.height.invalid", "height");
    // Placement business/map
    private static final ExceptionCode TABLE_PLACEMENT_OUTSIDE_MAP = ExceptionCode.of("tables.placement.outside_map");
    private static final ExceptionCode TABLE_PLACEMENT_OVERLAPS_UNUSED_AREA = ExceptionCode.of("tables.placement.overlaps_unused_area");
    private static final ExceptionCode TABLE_PLACEMENT_OVERLAPS_ANOTHER_TABLE = ExceptionCode.of("tables.placement.overlaps_another_table");
    private static final ExceptionCode TABLE_PLACEMENT_GAP_INVALID = ExceptionCode.of("tables.placement.gap.invalid", "gap");
    private static final ExceptionCode TABLE_PLACEMENT_TOO_CLOSE_TO_ANOTHER_TABLE = ExceptionCode.of("tables.placement.too_close_to_another_table");

    private TableDomainExceptions() {}

    /** Status **/
    public static BusinessConflictException statusChangeNotAllowed(TableStatus from, TableStatus to) {return new BusinessConflictException(STATUS_CHANGE_NOT_ALLOWED, from, to);}

    /** Code **/
    public static InvalidValueException blankTableCode() {
        return new InvalidValueException(TABLE_CODE_BLANK);
    }
    public static InvalidValueException tableCodeTooLong(int maxLength) {return new InvalidValueException(TABLE_CODE_TOO_LONG, maxLength);}
    public static InvalidValueException invalidTableCodeFormat(String value) {return new InvalidValueException(TABLE_CODE_INVALID_FORMAT, value);}

    /** Capacity **/
    public static BusinessConflictException tableCapacityChangeNotAllowedInStatus(TableStatus actualStatus, String allowedStatuses) {
        return new BusinessConflictException(TABLE_CAPACITY_CHANGE_NOT_ALLOWED_IN_STATUS, actualStatus, allowedStatuses);
    }
    public static InvalidValueException invalidCapacity(int min, int max) {return new InvalidValueException(TABLE_CAPACITY_INVALID, min,max);}

    /** Placement **/
    public static InvalidValueException invalidPlacementX(int min) {return new InvalidValueException(TABLE_PLACEMENT_X_INVALID, min);}
    public static InvalidValueException invalidPlacementY(int min) {return new InvalidValueException(TABLE_PLACEMENT_Y_INVALID, min);}
    public static InvalidValueException invalidPlacementWidth(int min) {return new InvalidValueException(TABLE_PLACEMENT_WIDTH_INVALID, min);}
    public static InvalidValueException invalidPlacementHeight(int min) {return new InvalidValueException(TABLE_PLACEMENT_HEIGHT_INVALID, min);}
    public static InvalidValueException invalidPlacementGap(int min) {return new InvalidValueException(TABLE_PLACEMENT_GAP_INVALID, min);}
    public static BusinessConflictException tablePlacementOutsideMap(int mapWidth, int mapHeight) {return new BusinessConflictException(TABLE_PLACEMENT_OUTSIDE_MAP, mapWidth, mapHeight);}
    public static BusinessConflictException tablePlacementOverlapsUnusedArea() {return new BusinessConflictException(TABLE_PLACEMENT_OVERLAPS_UNUSED_AREA);}
    public static BusinessConflictException tablePlacementOverlapsAnotherTable() {return new BusinessConflictException(TABLE_PLACEMENT_OVERLAPS_ANOTHER_TABLE);}
    public static BusinessConflictException tablePlacementTooCloseToAnotherTable(int minGap) {return new BusinessConflictException(TABLE_PLACEMENT_TOO_CLOSE_TO_ANOTHER_TABLE, minGap);}

}