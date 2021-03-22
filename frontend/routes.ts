import { Flow } from "@vaadin/flow-frontend";
import { Route } from "@vaadin/router";
import "./views/main/main-view";

const { serverSideRoutes } = new Flow({
  imports: () => import("../target/frontend/generated-flow-imports"),
});

export type ViewRoute = Route & { title?: string; children?: ViewRoute[] };

export const views: ViewRoute[] = [
  // for client-side, place routes below (more info https://vaadin.com/docs/v19/flow/typescript/creating-routes.html)
  {
    path: "private-ts",
    component: "private-ts-view",
    title: "Private TS",
    action: async () => {
      await import("./views/private-ts/private-ts-view");
    },
  },
  {
    path: "public-ts",
    component: "public-ts-view",
    title: "Public TS",
    action: async () => {
      await import("./views/public-ts/public-ts-view");
    },
  },
];
export const routes: ViewRoute[] = [
  {
    path: "",
    component: "main-view",
    children: [
      ...views,
      // for server-side, the next magic line sends all unmatched routes:
      ...serverSideRoutes, // IMPORTANT: this must be the last entry in the array
    ],
  },
];
