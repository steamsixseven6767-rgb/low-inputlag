package dev.lowinputlag;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class LowInputLagConfigScreen {
    private LowInputLagConfigScreen() {}

    public static Screen create(Screen parent) {
        LowInputLagConfig cfg = LowInputLagConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Low Input Lag"))
                .setSavingRunnable(LowInputLagConfig::save);

        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("Основные"));

        general.addEntry(eb.startBooleanToggle(Text.literal("Мод включён"), cfg.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Главный выключатель мода."))
                .setSaveConsumer(v -> cfg.enabled = v)
                .build());

        general.addEntry(eb.startBooleanToggle(Text.literal("Применять камеру каждый кадр"), cfg.applyEveryFrame)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Повторно применяет движение мыши прямо перед отрисовкой мира."))
                .setSaveConsumer(v -> cfg.applyEveryFrame = v)
                .build());

        general.addEntry(eb.startBooleanToggle(Text.literal("Опрос событий перед камерой"), cfg.pollEventsBeforeLook)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Даёт самые свежие данные, но может менять ощущение чувствительности. Выключи, если камера стала быстрее или есть глюки."))
                .setSaveConsumer(v -> cfg.pollEventsBeforeLook = v)
                .build());

        general.addEntry(eb.startBooleanToggle(Text.literal("Отключить сглаживание камеры"), cfg.disableSmoothing)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Принудительно выключает кинематографическую камеру в игре."))
                .setSaveConsumer(v -> cfg.disableSmoothing = v)
                .build());

        general.addEntry(eb.startFloatField(Text.literal("Множитель чувствительности"), cfg.sensitivityMultiplier)
                .setDefaultValue(1.0f)
                .setMin(0.1f)
                .setMax(10.0f)
                .setTooltip(Text.literal("1.0 = ванильная чувствительность."))
                .setSaveConsumer(v -> cfg.sensitivityMultiplier = v)
                .build());

        general.addEntry(eb.startBooleanToggle(Text.literal("Отладочный лог"), cfg.debugLog)
                .setDefaultValue(false)
                .setSaveConsumer(v -> cfg.debugLog = v)
                .build());

        general.addEntry(eb.startFloatField(Text.literal("Задержка включения (сек)"), cfg.enableDelaySeconds)
                .setDefaultValue(3.0f)
                .setMin(0.0f)
                .setMax(60.0f)
                .setTooltip(Text.literal("Через сколько секунд после входа в мир мод включается сам."))
                .setSaveConsumer(v -> cfg.enableDelaySeconds = v)
                .build());

        return builder.build();
    }
}
