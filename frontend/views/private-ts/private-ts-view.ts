import '@vaadin/vaadin-button';
import '@vaadin/vaadin-text-field';
import { BalanceEndpoint } from 'Frontend/generated/BalanceEndpoint';
import { appStore } from 'Frontend/stores/app-store';
import { customElement, html, internalProperty } from 'lit-element';
import { View } from '../view';

@customElement('private-ts-view')
export class PrivateTSView extends View {
  @internalProperty()
  private balance: number = 0;

  render() {
    return html`
      <div style="display:flex;flex-direction:column;align-items:flex-start;padding: var(--lumo-space-m);">
        <span>Hello ${appStore.user!.username}, your bank account balance is $${this.balance}.</span>

        <vaadin-button @click="${this.applyForLoan}">Apply for a loan</vaadin-button>
      </div>
    `;
  }
  async applyForLoan() {
    await BalanceEndpoint.applyForLoan();
    this.balance = await BalanceEndpoint.getBalance();
  }

  async connectedCallback() {
    super.connectedCallback();
    this.balance = await BalanceEndpoint.getBalance();
  }
}
