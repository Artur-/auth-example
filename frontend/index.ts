import { Router, RouterLocation } from '@vaadin/router';
import { UserInfoEndpoint } from './generated/UserInfoEndpoint';
import { isServerSideRoute, routes } from './routes';
import { appStore } from './stores/app-store';

export const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);

window.addEventListener('vaadin-router-location-changed', (e) => {
  const location: RouterLocation = (e as CustomEvent).detail.location;
  appStore.setLocation(location, isServerSideRoute(location));
  const title = appStore.currentViewTitle;
  if (title) {
    document.title = title + ' | ' + appStore.applicationName;
  } else {
    document.title = appStore.applicationName;
  }
});

UserInfoEndpoint.getUserInfo()
  .then((info) => {
    if (info) {
      appStore.user = info;
    }
  })
  .catch((e) => console.log('Ignoring user info error', e));
