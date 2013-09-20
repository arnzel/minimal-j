package ch.openech.mj.vaadin.toolkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import ch.openech.mj.toolkit.ExportHandler;

import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class VaadinExportDialog extends Window {
	
	private Link link;
	private PipedOutputStream pipedOutputStream = new PipedOutputStream();
	
	public VaadinExportDialog(UI ui, String title, final ExportHandler exportHandler) {
		super(title);
		
		try {
			HorizontalLayout horizontalLayout = new HorizontalLayout();
			final PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
            
    		StreamSource ss = new StreamSource() {
                @Override
                public InputStream getStream() {
                	VaadinExportDialog.this.close();
                	exportHandler.export(pipedOutputStream);
                    return pipedInputStream;
                }
            };
            StreamResource sr = new StreamResource(ss, "export.xml");
			sr.setMIMEType("text/xml");
			sr.setCacheTime(0);
			link = new Link("Link to Download", sr);
			
			horizontalLayout.addComponent(link);
			
			setContent(horizontalLayout);
			
			setModal(true);
			ui.addWindow(this);
		} catch (IOException x) {
        	x.printStackTrace();
        }
	}

}
