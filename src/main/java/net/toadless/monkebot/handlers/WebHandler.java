package net.toadless.monkebot.handlers;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.core.JavalinConfig;
import io.javalin.http.Context;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.toadless.monkebot.Monke;
import net.toadless.monkebot.objects.bot.ConfigOption;
import net.toadless.monkebot.web.guilds.ModifyRoute;
import net.toadless.monkebot.web.info.InfoRoute;
import net.toadless.monkebot.web.invite.InviteBotRoute;
import net.toadless.monkebot.web.shards.ShardsRoute;
import org.jetbrains.annotations.NotNull;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class WebHandler
{
    private final Monke monke;
    private final Javalin javalin;

    public WebHandler(@NotNull Monke monke)
    {
        this.monke = monke;
        this.javalin = Javalin
                .create(JavalinConfig::enableCorsForAllOrigins)
                .routes(() ->
                {
                    path("/shards", () -> ApiBuilder.get(new ShardsRoute(this)));
                    path("/info", () -> ApiBuilder.get(new InfoRoute(this)));

                    path("/invite", () ->
                    {
                        ApiBuilder.get(new InviteBotRoute(this));
                        path("/bot", () -> get(new InviteBotRoute(this)));
                    });

                    path("/health", () -> get(ctx -> ctx.result("Healthy")));

                    path("/modify", () -> get(new ModifyRoute(this)));
                }).start(4444);
    }

    public Monke getMonke()
    {
        return monke;
    }

    public Javalin getJavalin()
    {
        return javalin;
    }

    public void ok(Context context, DataObject payload)
    {
        result(context, 200, payload);
    }

    public void result(Context ctx, int code, DataObject data)
    {
        ctx.header("Content-Type", "application/json");
        ctx.status(code);
        ctx.result(data.toString());
    }
}