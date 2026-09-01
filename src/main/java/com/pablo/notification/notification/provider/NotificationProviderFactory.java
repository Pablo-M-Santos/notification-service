package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.domain.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationProviderFactory {

    private final Map<NotificationChannel, NotificationProvider> providers;

    public NotificationProviderFactory(List<NotificationProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(
                        NotificationProvider::getChannel,
                        Function.identity()
                ));
    }

    public NotificationProvider getProvider(NotificationChannel channel) {

        NotificationProvider provider = providers.get(channel);

        if (provider == null) {
            throw new IllegalArgumentException(
                    "Nenhum provider encontrado para o canal: " + channel
            );
        }

        return provider;
    }
}