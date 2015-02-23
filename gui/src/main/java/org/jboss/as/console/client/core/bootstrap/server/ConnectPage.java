package org.jboss.as.console.client.core.bootstrap.server;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.as.console.client.widgets.ContentDescription;
import org.jboss.ballroom.client.widgets.ContentHeaderLabel;
import org.jboss.ballroom.client.widgets.window.DialogueOptions;
import org.jboss.ballroom.client.widgets.window.WindowContentBuilder;

import java.util.List;

/**
 * @author Harald Pehl
 */
public class ConnectPage implements IsWidget {

    private final BootstrapServerSetup serverSetup;
    private final BootstrapServerDialog serverDialog;
    private final BootstrapServerStore serverStore;
    private BootstrapServerTable table;

    public ConnectPage(final BootstrapServerSetup serverSetup, final BootstrapServerDialog serverDialog) {
        this.serverSetup = serverSetup;
        this.serverDialog = serverDialog;
        this.serverStore = new BootstrapServerStore();
    }

    @Override
    public Widget asWidget() {
        FlowPanel content = new FlowPanel();
        content.add(new ContentHeaderLabel("Connect to Management Interface"));
        content.add(new ContentDescription("Pick a management interface from the list below or add a new one."));

        table = new BootstrapServerTable(serverDialog);
        content.add(table);

        final Label connectErrorMessages = new Label();
        connectErrorMessages.setStyleName("error-panel");
        content.add(connectErrorMessages);

        DialogueOptions options = new DialogueOptions(
                "Connect",
                new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        final BootstrapServer server = table.getSelectedServer();
                        if (server == null) {
                            connectErrorMessages.setText("Please select a management interface.");
                        } else {
                            serverSetup.pingServer(server, new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(final Throwable caught) {
                                    connectErrorMessages.setText("The selected management interface does not respond.");
                                }

                                @Override
                                public void onSuccess(final Void result) {
                                    serverSetup.onConnect(server);
                                }
                            });
                        }
                    }
                },
                "",
                new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent event) {
                        // not used
                    }
                }
        ).showCancel(false);

        return new WindowContentBuilder(content, options).build();
    }

    void reset() {
        List<BootstrapServer> servers = serverStore.load();
        table.getDataProvider().setList(servers);
        table.getCellTable().selectDefaultEntity();
    }
}
