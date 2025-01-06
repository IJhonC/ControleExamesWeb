package com.sa.appexamelaboratorio.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sa.appexamelaboratorio.model.Laboratorio;
import com.sa.appexamelaboratorio.model.Usuario;
import com.sa.appexamelaboratorio.service.LabService;
import com.sa.appexamelaboratorio.service.UsuarioService;

@RequestMapping("/lab")
@Controller
public class LabController {
    @Autowired
    private LabService labService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastrar")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("laboratorio", new Laboratorio());
        return "cadLaboratorio";
    }

    @PostMapping("/cadastrar")
    public String cadastrarLaboratorio(@RequestParam String nome,
            @RequestParam String rua,
            @RequestParam Integer numero,
            @RequestParam String bairro,
            @RequestParam String cidade,
            @RequestParam String estado,
            @RequestParam String telefone,
            @RequestParam String email,
            Model model) {

        // Concatenando as partes do endereço
        String enderecoCompleto = rua + ", " + numero + " - " + bairro + ", " + cidade + " - " + estado;

        // Criando o objeto Laboratório
        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setNome(nome);
        laboratorio.setEndereco(enderecoCompleto); // Atribuindo o endereço concatenado
        laboratorio.setTelefone(telefone);
        laboratorio.setEmail(email);

        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Usuario> user = usuarioService.buscarPorEmail(emailUser);
        laboratorio.setUsuario(user.get());

        // Salvando no banco de dados
        labService.salvarLab(laboratorio);

        // Adicionando mensagem de sucesso
        model.addAttribute("successMessage", "Laboratório cadastrado com sucesso!");

        return "cadLaboratorio";
    }

    @GetMapping("/listar")
    public String listarLaboratorios(Model model) {
        model.addAttribute("lab", labService.listarLab());
        return "listLab";
    }

    @PostMapping("/deletar/{id}")
    public String deletarLaboratorio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            labService.deletarPorId(id);
            redirectAttributes.addFlashAttribute("successMessage", "Laboratorio excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Não é possível excluir o laboratório, pois ele possui exames associados!");
        }
        return "redirect:/lab/listar";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {
        Laboratorio laboratorio = labService.buscarPorId(id);

        // Variáveis para armazenar as partes do endereço
        String rua = "";
        String numero = "";
        String bairro = "";
        String cidade = "";
        String estado = "";

        if (laboratorio != null) {
            // Supondo que o endereço está armazenado em laboratorio.getEndereco()
            String endereco = laboratorio.getEndereco();

            // Vamos dividir o endereço pelas vírgulas
            String[] partes = endereco.split(",");

            // Agora, extrair as partes específicas
            if (partes.length == 3) {
                // Exemplo: "rua teste", "2278 - gavea", "APARECIDA - PB"
                rua = partes[0].trim();
                String[] numeroEBairro = partes[1].split(" - ");
                if (numeroEBairro.length == 2) {
                    numero = numeroEBairro[0].trim();
                    bairro = numeroEBairro[1].trim();
                }
                String[] cidadeEEstado = partes[2].split(" - ");
                if (cidadeEEstado.length == 2) {
                    cidade = cidadeEEstado[0].trim();
                    estado = cidadeEEstado[1].trim();
                }
            }

            // Adicionando os dados ao modelo
            model.addAttribute("rua", rua);
            model.addAttribute("numero", numero);
            model.addAttribute("bairro", bairro);
            model.addAttribute("cidade", cidade);
            model.addAttribute("estado", estado);
            model.addAttribute("laboratorio", laboratorio);

            return "editarLaboratorio";
        } else {
            return "redirect:/lab/listar";
        }
    }

    @PostMapping("/editar")
    public String editarLaboratorio(
            @RequestParam String nome,
            @RequestParam String rua,
            @RequestParam String numero,
            @RequestParam String bairro,
            @RequestParam String cidade,
            @RequestParam String estado,
            @RequestParam String telefone,
            @RequestParam String email,
            @ModelAttribute Laboratorio laboratorio,
            RedirectAttributes redirectAttributes) {

        // Concatenar os campos de endereço
        String endereco = rua + ", " + numero + " - " + bairro + ", " + cidade + " - " + estado;

        laboratorio.setNome(nome);
        laboratorio.setEndereco(endereco); // Definindo o endereço concatenado
        laboratorio.setTelefone(telefone);
        laboratorio.setEmail(email);

        // Salvar o laboratório com o endereço gerado
        labService.salvarLab(laboratorio);

        // Adicionar mensagem de sucesso e redirecionar
        redirectAttributes.addFlashAttribute("success", "Laboratório alterado com sucesso!");
        return "redirect:/lab/listar";
    }
}
