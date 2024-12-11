const handlePhone = (event) => {
    let input = event.target
    input.value = phoneMask(input.value)
}

const phoneMask = (value) => {
    if (!value) return ""
    value = value.replace(/\D/g, '')
    value = value.replace(/(\d{2})(\d)/, "($1) $2")
    value = value.replace(/(\d)(\d{4})$/, "$1-$2")
    return value
}

$(document).ready(function () {
    // Carregar estados
    $.ajax({
        url: 'https://brasilapi.com.br/api/ibge/uf/v1', // API para buscar estados
        type: 'GET',
        success: function (data) {
            let select = document.getElementById('floatingInputEstado');
            // Preencher o campo de estado com os estados recebidos
            data.forEach(function (estado) {
                if (estado.sigla == select.value) {
                    $('#floatingInputEstado').append('<option selected value="' + estado.sigla + '">' + estado.nome + '</option>');
                } else {
                    $('#floatingInputEstado').append('<option value="' + estado.sigla + '">' + estado.nome + '</option>');
                }
            });
        },
        error: function () {
            alert('Erro ao carregar os estados.');
        }
    });

    // flag para não limpar o campo cidade ao carregar o formulário
    let canExecute = false;
    // Quando o estado for alterado, carregar as cidades
    $('#floatingInputEstado').change(function () {
        var estado = $(this).val();

        //Limpa o campo cidade
        if (canExecute) {
            $('#floatingInputCidade').html('<option value="">Selecione uma cidade</option>');
        }

        if (estado) {
            // Requisição AJAX para obter as cidades com base no estado
            $.ajax({
                url: 'https://brasilapi.com.br/api/ibge/municipios/v1/' + estado, // API para buscar cidades do estado
                type: 'GET',
                success: function (cidades) {
                    let select = document.getElementById('floatingInputCidade');
                    // Habilita o campo de cidade
                    if ($('#floatingInputCidade').prop('disabled')) {
                        $('#floatingInputCidade').prop('disabled', false);
                    }

                    // Preencher o campo de cidade com as cidades recebidas
                    cidades.forEach(function (cidade) {
                        if (cidade.nome == select.value) {
                            $('#floatingInputCidade').append('<option selected value="' + cidade.nome + '">' + cidade.nome + '</option>');
                        } else {
                            $('#floatingInputCidade').append('<option value="' + cidade.nome + '">' + cidade.nome + '</option>');
                        }
                    });
                    canExecute = true;
                },
                error: function () {
                    alert('Erro ao carregar as cidades.');
                }
            });
        } else {
            // Desabilita o campo de cidade caso o estado esteja vazio
            $('#floatingInputCidade').prop('disabled', true);
        }
    });
});
