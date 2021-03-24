import '@vaadin/vaadin-app-layout';
import { AppLayoutElement } from '@vaadin/vaadin-app-layout';
import '@vaadin/vaadin-app-layout/vaadin-drawer-toggle';
import '@vaadin/vaadin-avatar/vaadin-avatar';
import '@vaadin/vaadin-tabs';
import '@vaadin/vaadin-tabs/vaadin-tab';
import { customElement, html } from 'lit-element';
import { router } from '../../index';
import { appStore } from '../../stores/app-store';
import { Layout } from '../view';
import styles from './main-view.css';

interface RouteInfo {
  path: string;
  title: string;
}
@customElement('main-view')
export class MainView extends Layout {
  static get styles() {
    return [styles];
  }

  render() {
    return html`
      <vaadin-app-layout primary-section="drawer">
        <header slot="navbar" theme="dark">
          <vaadin-drawer-toggle></vaadin-drawer-toggle>
          <h1>${appStore.currentViewTitle}</h1>
          <vaadin-avatar name="${appStore.name}"></vaadin-avatar>
        </header>

        <div slot="drawer">
          <div id="logo">
            <img style="text-align: center" src="images/logo.jpg" alt="${appStore.applicationName} logo" />
          </div>
          <hr />
          <vaadin-tabs orientation="vertical" theme="minimal" .selected=${this.getSelectedViewRoute()}>
            ${this.getMenuRoutes().map(
              (viewRoute) => html`
                <vaadin-tab>
                  <a href="${router.urlForPath(viewRoute.path)}" tabindex="-1">${viewRoute.title}</a>
                </vaadin-tab>
              `
            )}
          </vaadin-tabs>
        </div>
        <slot></slot>
      </vaadin-app-layout>
    `;
  }

  connectedCallback() {
    super.connectedCallback();

    this.reaction(
      () => appStore.location,
      () => {
        AppLayoutElement.dispatchCloseOverlayDrawerEvent();
      }
    );
  }

  private getMenuRoutes(): RouteInfo[] {
    const views: RouteInfo[] = [
      {
        path: 'public-java',
        title: 'Public Java',
      },
      {
        path: 'public-ts',
        title: 'Public TS',
      },
      {
        path: 'private-java',
        title: 'Private Java',
      },
      {
        path: 'private-ts',
        title: 'Private TS',
      },
    ];
    return views;
  }

  private getSelectedViewRoute(): number {
    return this.getMenuRoutes().findIndex((viewRoute) => viewRoute.path == appStore.location);
  }
}
