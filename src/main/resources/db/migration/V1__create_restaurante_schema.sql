create table mesas (
    id bigserial primary key,
    numero integer not null unique,
    descricao varchar(100),
    capacidade integer not null default 4,
    status varchar(20) not null default 'LIVRE',
    check (status in ('LIVRE', 'OCUPADA', 'RESERVADA', 'INATIVA'))
);

create table categorias_produtos (
    id bigserial primary key,
    nome varchar(100) not null unique,
    ativa boolean not null default true
);

create table produtos (
    id bigserial primary key,
    categoria_id bigint not null references categorias_produtos(id),
    nome varchar(150) not null,
    descricao text,
    preco numeric(10,2) not null check (preco >= 0),
    disponivel boolean not null default true,
    tempo_preparo_minutos integer,
    criado_em timestamp not null default current_timestamp,
    atualizado_em timestamp
);


create index idx_produtos_nome on produtos(nome);
create index idx_produtos_categoria on produtos(categoria_id);

create table pedidos (
    id bigserial primary key,
    mesa_id bigint not null references mesas(id),
    data_abertura timestamp not null default current_timestamp,
    data_fechamento timestamp,
    status varchar(30) not null default 'ABERTO',
    observacao text,
    check (status in ('ABERTO', 'EM_PREPARO', 'PRONTO', 'ENTREGUE', 'FECHADO', 'CANCELADO'))
);


create index idx_pedidos_mesa on pedidos(mesa_id);
create index idx_pedidos_status on pedidos(status);
create index idx_pedidos_data_abertura on pedidos(data_abertura);


create table pedido_itens (
    id bigserial primary key,
    pedido_id bigint not null references pedidos(id),
    produto_id bigint not null references produtos(id),
    quantidade integer not null check (quantidade > 0),
    preco_unitario numeric(10,2) not null check (preco_unitario >= 0),
    observacao text,
    status varchar(30) not null default 'PENDENTE',
    check (status in ('PENDENTE', 'EM_PREPARO', 'PRONTO', 'ENTREGUE', 'CANCELADO'))
);



create index idx_pedido_itens_pedido on pedido_itens(pedido_id);
create index idx_pedido_itens_produto on pedido_itens(produto_id);
create index idx_pedido_itens_status on pedido_itens(status);



create table pagamentos (
    id bigserial primary key,
    pedido_id bigint not null references pedidos(id),
    valor numeric(10,2) not null check (valor >= 0),
    forma_pagamento varchar(30) not null,
    status varchar(30) not null default 'PENDENTE',
    codigo_transacao_externa varchar(100),
    data_pagamento timestamp,
    criado_em timestamp not null default current_timestamp,
    check (forma_pagamento in ('DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX')),
    check (status in ('PENDENTE', 'APROVADO', 'RECUSADO', 'CANCELADO'))
);


create index idx_pagamentos_pedido on pagamentos(pedido_id);
create index idx_pagamentos_status on pagamentos(status);


create table fechamentos_conta (
    id bigserial primary key,
    pedido_id bigint not null unique references pedidos(id),
    subtotal numeric(10,2) not null check (subtotal >= 0),
    taxa_servico numeric(10,2) not null default 0 check (taxa_servico >= 0),
    desconto numeric(10,2) not null default 0 check (desconto >= 0),
    total numeric(10,2) not null check (total >= 0),
    data_fechamento timestamp not null default current_timestamp
);
