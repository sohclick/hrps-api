package com.sohclick.hrps.notification.service.mapper;

import com.sohclick.hrps.notification.domain.Notification;
import com.sohclick.hrps.notification.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {}
