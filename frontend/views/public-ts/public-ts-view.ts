import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import { customElement, html } from 'lit-element';
import { View } from '../view';

@customElement('public-ts-view')
export class PublicTSView extends View {
  render() {
    return html`<div style="display:flex;flex-direction:column;height:100%">
      <h1 style="text-align:center">Welcome to the Bank of Vaadin</h1>
      <img style="max-width: 100%; min-height: 0" src="images/bank.jpg" />
      <p>We are very great and have great amounts of money.</p>
    </div>`;
  }
}
