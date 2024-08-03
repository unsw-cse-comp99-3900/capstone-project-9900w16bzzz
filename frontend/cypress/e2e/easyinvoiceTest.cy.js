/**
 * Easyinvoice Test
 * 0. Visit homepage 
 * 1. Create a user through register page
 * 2. login use test account
 * 3. create an invoice by save directly
 * 4. create an json format invoice by convert to UBL and validate
 * 5. search file and send
 * 6. search file and delete
 * 7. logout
 */

describe('easyinvoice path', () => {
    beforeEach(() => {
        cy.restoreLocalStorage()
    });
    //After each test we save local storage
    afterEach(() => {
        cy.saveLocalStorage()
    });
    //step0 start
    it('Step0: should navigate to homepage successful', () => {
        cy.visit('localhost:8080/');
        cy.url().should('include', 'http://localhost:8080');
        cy.get('h1').should('have.text', 'Eazy Invoice');
        //cy.get('span').should('have.text', 'Easy');
    });

    it('Step1: should navigate to register page successfylly', () => {
        cy.get('a.header-btn').should('contain', 'START').click();
        cy.get('input[placeholder="Email"]').focus().type('test@test.com');
        cy.get('input[placeholder="Username"]').focus().type('test');
        cy.get('input[placeholder="Password"]').focus().type('test123');
        cy.get('input[placeholder="Confirm password"]').focus().type('test123');
        //click sign up button
        cy.contains('button', 'Sign up').click();
        //sign up successful, have popup message
        //automaticly goto homepage
        cy.url().should('include', 'localhost:8080/');
    });

    it('Step2: login use test account', () => {
        cy.visit('localhost:8080/');
        cy.url().should('include', 'http://localhost:8080');
        cy.get('a.header-btn').should('contain', 'START').click();
        cy.contains('a', 'Already have an account? Login').click();
        cy.get('input[placeholder="Email"]').focus().type('test@test.com');
        cy.get('input[placeholder="Password"]').focus().type('test123');
        cy.contains('button', 'Log in').click();
        //automaticly go to homepage
        cy.url().should('include', 'localhost:8080/');
    });

    it('Step3: create an invoice by save directly', () => {
        cy.visit('localhost:8080/');
        cy.url().should('include', 'http://localhost:8080');
        cy.get('label.menu-icon').click();
        cy.contains('a', 'Create invoice').click();
        cy.get('h1').should('have.text', 'Get Started');
        //cy.get('span').should('have.text', 'Get');
        cy.get('#Layer_1').should('be.visible').click();
        cy.get('label[for="file-upload"]').should('be.visible').click();
        const filePath = 'file2.JSON'
        const fileName = 'file2.JSON'
        cy.fixture(filePath).then(fileContent => {
            cy.get('input[type="file"]').attachFile({
              fileContent,
              fileName,
              mimeType: 'application/json'
            });
        });
        cy.get('[data-cy="arrow-button"]').click();
        //cy.pause();
        //step 2
        //cy.get('h1').should('have.text', 'Step 2  Select save format');
        //cy.get('span').should('have.text', 'Step 2');
        cy.contains('Save Directly').click();
        cy.get('[data-cy="arrow-button"]').click();
        //cy.get('#Layer_1').should('be.visible').click();
        cy.wait(10000)//wait 20s
        //successful
        cy.get('h1').should('have.text', 'Invoice created!You can check your invoice in the MY INVOICE page.');
    });
    
    it('Step4: create an json format invoice by convert to UBL and validate', () => {
        cy.visit('localhost:8080/');
        cy.url().should('include', 'http://localhost:8080');
        cy.get('label.menu-icon').click();
        cy.contains('a', 'Create invoice').click();
        //create start
        cy.get('h1').should('have.text', 'Get Started');
        //cy.get('span').should('have.text', 'Get');
        //next button
        cy.get('#Layer_1').should('be.visible').click();
        //step 1
        cy.get('h1').should('have.text', 'Step 1  Upload File');
        //cy.get('span').should('have.text', 'Step 1');
        cy.get('label[for="file-upload"]').should('be.visible').click();
        const fileName = 'final_json_sample.json'
        const filePath = 'final_json_sample.json';
        cy.fixture(filePath).then(fileContent => {
            cy.get('input[type="file"]').attachFile({
              fileContent,
              fileName,
              mimeType: 'application/json'
            });
        });
        cy.get('[data-cy="arrow-button"]').click();
        //cy.contains('button', 'Save Directly').click();
        //step2
        //cy.get('h1').should('have.text', 'Step 2  Select save format');
        //cy.get('span').should('have.text', 'Step 2');
        //cy.get('button').should('contain', 'Convert to UBL').click();
        cy.contains('Convert to UBL').click();
        //cy.contains('button', 'Convert to UBL').click();
        cy.get('[data-cy="arrow-button"]').click();
        //last step
        cy.get('h1').should('have.text', 'Last Step Convert to UBL');
        //cy.get('span').should('have.text', 'Last Step');
        //select rules
        cy.contains('UBL Standard Rule 1.0.10').click();
        cy.contains('Peppol Standard Rule 1.0.10').click();
        cy.contains('Peppol Small Business Rule 1.0.10').click();
        cy.contains('button', 'Validate').click();
        cy.wait(10000);//wait 20s
        //successful
        cy.get('h1').should('have.text', 'Invoice validated!You can check your invoice and validate status in the MY INVOICE page.OR');
        cy.contains('button', 'Check Validation Report!').click();
        cy.get('h1').should('have.text', 'Validation Report');
        //cy.get('span').should('have.text', 'Validation');
    });

    it('Step5: search file and send', () => {
        cy.visit('localhost:8080/my-invoice');
        cy.url().should('include', 'localhost:8080/my-invoice');
        cy.get('input[placeholder="Search..."]').focus().type('final_json_sample');
        //cy.contains('button','final_json_sample').click();
        cy.contains('final_json_sample').click();
        cy.get('input[placeholder="Email address"]').focus().type('serina.wu1999@gmail.com');
        cy.contains('button', 'Send').click();
    });

    it('Step6: search file and delete file', () => {
        cy.visit('localhost:8080/my-invoice');
        cy.url().should('include', 'localhost:8080/my-invoice');
        cy.get('input[placeholder="Search..."]').focus().type('final_json_sample');
        cy.contains('button', 'Delete').click();
        //cy.contains('Delete successful!').should('be.visible');
    });

    it('Step7: logout', () => {
        cy.visit('localhost:8080/');
        cy.url().should('include', 'http://localhost:8080');
        cy.get('label.menu-icon').click();
        cy.contains('test').click();
        cy.contains('a', 'Log out').click();
        //cy.contains('Log out successfully').should('be.visible');
    });
})