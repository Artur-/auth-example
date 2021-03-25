import { css, customElement, html, LitElement } from 'lit-element';

@customElement('empty-view')
export class EmptyView extends LitElement {
  static get styles() {
    return css``;
  }
  render() {
    return html` <div>Intentionally blank</div> `;
  }
}
