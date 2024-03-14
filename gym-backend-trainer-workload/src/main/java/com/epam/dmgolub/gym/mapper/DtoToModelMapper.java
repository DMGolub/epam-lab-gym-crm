package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.dto.WorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DtoToModelMapper {

	public abstract WorkloadUpdateRequest mapToWorkloadUpdateRequest(WorkloadUpdateRequestDTO workloadUpdateRequestDTO);
}
