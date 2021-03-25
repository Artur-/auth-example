import '@vaadin/vaadin-app-layout';
import { AppLayoutElement } from '@vaadin/vaadin-app-layout';
import '@vaadin/vaadin-app-layout/vaadin-drawer-toggle';
import '@vaadin/vaadin-avatar/vaadin-avatar';
import '@vaadin/vaadin-tabs';
import '@vaadin/vaadin-tabs/vaadin-tab';
import { customElement, html } from 'lit-element';
import { nothing } from 'lit-html';
import { router } from '../../index';
import { appStore } from '../../stores/app-store';
import { Layout } from '../view';
import styles from './main-view.css';

interface RouteInfo {
  path: string;
  title: string;
  routerignore?: boolean;
  requiresAuthentication?: boolean;
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
          ${appStore.user
            ? html`<vaadin-avatar img="${appStore.user.imageUrl}" name="${appStore.user.username}"></vaadin-avatar>`
            : html`<a router-ignore href="login">Sign in</a>`}
        </header>

        <div slot="drawer">
          <div id="logo">
            <a href="">
              <img style="text-align: center" src="images/logo.jpg" alt="${appStore.applicationName} logo" />
            </a>
          </div>
          <hr />
          <vaadin-tabs orientation="vertical" theme="minimal" .selected=${this.getSelectedViewRoute()}>
            ${this.getMenuRoutes().map(
              (viewRoute) => html`
                <vaadin-tab>
                  <a ?router-ignore=${viewRoute.routerignore} href="${router.urlForPath(viewRoute.path)}" tabindex="-1"
                    >${viewRoute.title}</a
                  >
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
        requiresAuthentication: true,
      },
      {
        path: 'private-ts',
        title: 'Private TS',
        requiresAuthentication: true,
      },
      {
        path: 'logout',
        title: 'Logout',
        routerignore: true,
        requiresAuthentication: true,
      },
    ];
    return views.filter((route) => !route.requiresAuthentication || !!appStore.user);
  }

  private getSelectedViewRoute(): number {
    return this.getMenuRoutes().findIndex((viewRoute) => viewRoute.path == appStore.location);
  }
}
