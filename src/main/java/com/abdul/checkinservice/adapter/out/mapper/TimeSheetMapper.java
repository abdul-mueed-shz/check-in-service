package com.abdul.checkinservice.adapter.out.mapper;

import com.abdul.checkinservice.adapter.out.persistence.entity.TimeSheet;
import com.abdul.checkinservice.domain.timesheet.model.TimeSheetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TimeSheetMapper {

    List<TimeSheetDto> toDtoList(List<TimeSheet> timeSheets);

    TimeSheetDto toDto(TimeSheet timeSheet);

    TimeSheet toEntity(TimeSheetDto timeSheetDto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TimeSheetDto toUpdatedDto(@MappingTarget TimeSheetDto existingDto, TimeSheetDto updatedDto);
}


