import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import { PublicEndpoint } from 'Frontend/generated/PublicEndpoint';
import { customElement, html, internalProperty } from 'lit-element';
import { View } from '../view';

@customElement('public-ts-view')
export class PublicTSView extends View {
  @internalProperty()
  private time: string = '';

  async connectedCallback() {
    super.connectedCallback();
    this.time = await PublicEndpoint.getServerTime();
  }

  render() {
    return html`<div style="display:flex;flex-direction:column;height:100%">
      <h1 style="text-align:center">Welcome to the TypeScript Bank of Vaadin</h1>
      <img style="max-width: 100%; min-height: 0" src="images/bank.jpg" />
      <p>We are very great and have great amounts of money.</p>
      <p>This page was updated ${this.time}</p>
    </div>`;
  }
}
