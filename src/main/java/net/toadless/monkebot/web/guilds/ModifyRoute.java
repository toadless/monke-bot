package net.toadless.monkebot.web.guilds;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.monkebot.handlers.WebHandler;
import net.toadless.monkebot.objects.cache.GuildSettingsCache;
import org.jetbrains.annotations.NotNull;

public class ModifyRoute implements Handler
{
    private final WebHandler webHandler;

    public ModifyRoute(WebHandler webHandler)
    {
        this.webHandler = webHandler;
    }

    @Override
    public void handle(@NotNull Context ctx)
    {
        // This route un-caches the provided guild to keep in matched with the database. (Dashboard implementation)
        try
        {
            long guildId = Long.parseLong(ctx.req.getHeader("id"));

            if (guildId == 0)
            {
                webHandler.result(ctx, 400, DataObject.empty().put("response", "bad request"));
                return;
            }

            GuildSettingsCache.removeCache(guildId);
            webHandler.ok(ctx, DataObject.empty().put("response", "OK"));
        } catch (Exception exception)
        {
            if (exception instanceof NumberFormatException)
            {
                webHandler.result(ctx, 400, DataObject.empty().put("response", "bad request"));
                return;
            }

            webHandler.result(ctx, 500, DataObject.empty().put("response", "internal server error"));
        }
    }
}