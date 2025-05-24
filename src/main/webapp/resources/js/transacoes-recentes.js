// JavaScript simplificado para transações recentes - sem paginação
class TransacoesDisplay {
    constructor() {
        this.transacoes = [];
        this.init();
    }

    init() {
        this.transacoes = Array.from(document.querySelectorAll('.transacao-item'));
        console.log(`Total de transações encontradas: ${this.transacoes.length}`);

        // Mostrar todas as transações
        this.showAllTransactions();

        // Esconder elementos de navegação/paginação já que não precisamos mais
        this.hideNavigationElements();
    }

    showAllTransactions() {
        // Garantir que todas as transações estejam visíveis
        this.transacoes.forEach((transacao, index) => {
            transacao.classList.remove('hidden');
            transacao.style.display = 'flex';
            console.log(`Mostrando transação ${index + 1}`);
        });

        console.log(`Todas as ${this.transacoes.length} transações estão sendo exibidas`);
    }

    hideNavigationElements() {
        // Esconder elementos de navegação
        const navigation = document.querySelector('.transacoes-navigation');
        if (navigation) {
            navigation.style.display = 'none';
            console.log('Navegação escondida - exibindo todas as transações');
        }

        // Esconder paginação se existir
        const paginationContainer = document.querySelector('.transacoes-pagination');
        if (paginationContainer) {
            paginationContainer.style.display = 'none';
            console.log('Paginação escondida - exibindo todas as transações');
        }
    }

    // Método para atualizar quando novas transações são carregadas
    refresh() {
        this.init();
    }
}

// Função global para inicializar a exibição das transações
function initTransacoesPagination() {
    const transacoesContainer = document.getElementById('transacoes-container');
    const transacoes = document.querySelectorAll('.transacao-item');

    console.log(`Inicializando exibição de transações - ${transacoes.length} transações encontradas`);

    if (transacoesContainer && transacoes.length > 0) {
        window.transacoesDisplay = new TransacoesDisplay();
        console.log('Sistema de transações inicializado com sucesso');
    } else {
        console.log('Nenhuma transação encontrada ou container não existe');

        // Debug adicional
        console.log('Container existe:', !!transacoesContainer);
        console.log('Quantidade de transações:', transacoes.length);
    }
}

// Função para scroll suave para o topo das transações
function scrollToTransacoes() {
    const transacoesCard = document.querySelector('.transacoes-card');
    if (transacoesCard) {
        transacoesCard.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });
    }
}

// Inicializar quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM carregado - preparando para inicializar transações');
});

// Exportar para uso global
window.initTransacoesPagination = initTransacoesPagination;
window.TransacoesDisplay = TransacoesDisplay;