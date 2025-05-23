// JavaScript para gerenciar a paginação das transações recentes
class TransacoesPagination {
    constructor() {
        this.itensPerPage = 5;
        this.currentPage = 1;
        this.totalPages = 1;
        this.transacoes = [];

        this.init();
    }

    init() {
        this.transacoes = document.querySelectorAll('.transacao-item');
        this.totalPages = Math.ceil(this.transacoes.length / this.itensPerPage);

        this.setupEventListeners();
        this.updatePagination();
        this.createPaginationButtons();
    }

    setupEventListeners() {
        const btnAnterior = document.getElementById('btnAnterior');
        const btnProximo = document.getElementById('btnProximo');

        if (btnAnterior) {
            btnAnterior.addEventListener('click', () => this.goToPreviousPage());
        }

        if (btnProximo) {
            btnProximo.addEventListener('click', () => this.goToNextPage());
        }
    }

    showPage(pageNumber) {
        if (pageNumber < 1 || pageNumber > this.totalPages) return;

        this.currentPage = pageNumber;

        // Esconder todas as transações
        this.transacoes.forEach(transacao => {
            transacao.classList.add('hidden');
        });

        // Mostrar transações da página atual
        const startIndex = (pageNumber - 1) * this.itensPerPage;
        const endIndex = startIndex + this.itensPerPage;

        for (let i = startIndex; i < endIndex && i < this.transacoes.length; i++) {
            this.transacoes[i].classList.remove('hidden');
        }

        this.updatePagination();
        this.updatePaginationButtons();
    }

    goToPreviousPage() {
        if (this.currentPage > 1) {
            this.showPage(this.currentPage - 1);
        }
    }

    goToNextPage() {
        if (this.currentPage < this.totalPages) {
            this.showPage(this.currentPage + 1);
        }
    }

    updatePagination() {
        const paginaInfo = document.getElementById('paginaInfo');
        const btnAnterior = document.getElementById('btnAnterior');
        const btnProximo = document.getElementById('btnProximo');

        if (paginaInfo) {
            paginaInfo.textContent = `${this.currentPage} de ${this.totalPages}`;
        }

        if (btnAnterior) {
            btnAnterior.disabled = this.currentPage <= 1;
            btnAnterior.classList.toggle('disabled', this.currentPage <= 1);
        }

        if (btnProximo) {
            btnProximo.disabled = this.currentPage >= this.totalPages;
            btnProximo.classList.toggle('disabled', this.currentPage >= this.totalPages);
        }

        // Esconder navegação se houver apenas uma página
        const navigation = document.querySelector('.transacoes-navigation');
        if (navigation) {
            navigation.style.display = this.totalPages <= 1 ? 'none' : 'flex';
        }
    }

    createPaginationButtons() {
        const paginacao = document.getElementById('paginacao');
        if (!paginacao || this.totalPages <= 1) return;

        paginacao.innerHTML = '';

        // Botão anterior
        const prevLi = document.createElement('li');
        prevLi.className = `page-item ${this.currentPage <= 1 ? 'disabled' : ''}`;
        prevLi.innerHTML = '<a class="page-link" href="#" aria-label="Anterior"><span aria-hidden="true">&laquo;</span></a>';
        prevLi.addEventListener('click', (e) => {
            e.preventDefault();
            this.goToPreviousPage();
        });
        paginacao.appendChild(prevLi);

        // Números das páginas
        const maxVisiblePages = 5;
        let startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
        let endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);

        // Ajustar o início se estivermos no final
        if (endPage - startPage + 1 < maxVisiblePages) {
            startPage = Math.max(1, endPage - maxVisiblePages + 1);
        }

        // Adicionar primeira página se necessário
        if (startPage > 1) {
            const firstLi = document.createElement('li');
            firstLi.className = 'page-item';
            firstLi.innerHTML = '<a class="page-link" href="#">1</a>';
            firstLi.addEventListener('click', (e) => {
                e.preventDefault();
                this.showPage(1);
            });
            paginacao.appendChild(firstLi);

            if (startPage > 2) {
                const ellipsisLi = document.createElement('li');
                ellipsisLi.className = 'page-item disabled';
                ellipsisLi.innerHTML = '<span class="page-link">...</span>';
                paginacao.appendChild(ellipsisLi);
            }
        }

        // Páginas visíveis
        for (let i = startPage; i <= endPage; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === this.currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
            li.addEventListener('click', (e) => {
                e.preventDefault();
                this.showPage(i);
            });
            paginacao.appendChild(li);
        }

        // Adicionar última página se necessário
        if (endPage < this.totalPages) {
            if (endPage < this.totalPages - 1) {
                const ellipsisLi = document.createElement('li');
                ellipsisLi.className = 'page-item disabled';
                ellipsisLi.innerHTML = '<span class="page-link">...</span>';
                paginacao.appendChild(ellipsisLi);
            }

            const lastLi = document.createElement('li');
            lastLi.className = 'page-item';
            lastLi.innerHTML = `<a class="page-link" href="#">${this.totalPages}</a>`;
            lastLi.addEventListener('click', (e) => {
                e.preventDefault();
                this.showPage(this.totalPages);
            });
            paginacao.appendChild(lastLi);
        }

        // Botão próximo
        const nextLi = document.createElement('li');
        nextLi.className = `page-item ${this.currentPage >= this.totalPages ? 'disabled' : ''}`;
        nextLi.innerHTML = '<a class="page-link" href="#" aria-label="Próximo"><span aria-hidden="true">&raquo;</span></a>';
        nextLi.addEventListener('click', (e) => {
            e.preventDefault();
            this.goToNextPage();
        });
        paginacao.appendChild(nextLi);
    }

    updatePaginationButtons() {
        const paginacao = document.getElementById('paginacao');
        if (!paginacao) return;

        // Atualizar estado dos botões
        const buttons = paginacao.querySelectorAll('.page-item');
        buttons.forEach(button => {
            const link = button.querySelector('.page-link');
            const pageNum = parseInt(link?.textContent);

            if (pageNum) {
                button.classList.toggle('active', pageNum === this.currentPage);
            }
        });

        // Atualizar botões anterior/próximo
        const firstButton = buttons[0];
        const lastButton = buttons[buttons.length - 1];

        if (firstButton) {
            firstButton.classList.toggle('disabled', this.currentPage <= 1);
        }

        if (lastButton) {
            lastButton.classList.toggle('disabled', this.currentPage >= this.totalPages);
        }
    }

    // Método para atualizar quando novas transações são carregadas
    refresh() {
        this.init();
        this.showPage(1);
    }
}

// Função global para inicializar a paginação
function initTransacoesPagination() {
    const transacoesContainer = document.getElementById('transacoes-container');
    if (transacoesContainer && document.querySelectorAll('.transacao-item').length > 0) {
        window.transacoesPagination = new TransacoesPagination();
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

// Utilitários para responsividade
function handleResponsiveNavigation() {
    const navigation = document.querySelector('.transacoes-navigation');
    if (!navigation) return;

    const checkWidth = () => {
        if (window.innerWidth <= 576) {
            navigation.classList.add('mobile-nav');
        } else {
            navigation.classList.remove('mobile-nav');
        }
    };

    checkWidth();
    window.addEventListener('resize', checkWidth);
}

// Inicializar quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    handleResponsiveNavigation();
});

// Exportar para uso global
window.initTransacoesPagination = initTransacoesPagination;
window.TransacoesPagination = TransacoesPagination;