package com.oms.app.tables.application.port.in.command;

import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.vo.TableCapacity;
import com.oms.app.tables.domain.vo.TableCode;

import java.util.UUID;

public interface CreateTableUseCasePort {

    Result handle(Command cmd);


    record Command (
            TableCode code,
            TableCapacity capacity,
            TablePlacement placement
    ){

        public static Command of(String code, int capacity, int x, int y,int width,int height) {

            return new Command(
                    TableCode.of(code),
                    TableCapacity.of(capacity),
                    TablePlacement.of(x,y,width,height)
            );
        }

    }

    record Result(
            UUID tableId
    ){
        public static Result of(UUID tableId) {
            return new Result(tableId);
        }
    }
}
