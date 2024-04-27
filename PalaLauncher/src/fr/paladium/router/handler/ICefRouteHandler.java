package fr.paladium.router.handler;

import fr.paladium.router.request.CefRouteRequest;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;

public interface ICefRouteHandler<T> {
  CefRouteHandler handle(CefBrowser paramCefBrowser, CefFrame paramCefFrame, CefRouteRequest.RequestType paramRequestType, T paramT);
}


/* Location:              C:\Users\ant-1\Desktop\source\!\fr\paladium\router\handler\ICefRouteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */