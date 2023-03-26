package sisrh.rest;

import io.swagger.annotations.Api;
import sisrh.banco.Banco;
import sisrh.dto.Empregado;
import sisrh.dto.Solicitacao;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api
@Path("/solicitacao")
public class SolicitacaoRest {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarSolicitacoes() throws Exception {
        List<Solicitacao> lista = Banco.listarSolicitacoes();
        GenericEntity<List<Solicitacao>> entity = new GenericEntity<List<Solicitacao>>(lista){};
        return Response.ok().entity(entity).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterSolicitacao(@PathParam("id") String id) throws Exception {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{ \"mensagem\" : \"ID da solicitação não pode ser nulo!\" }").build();
        }

        try {
            Solicitacao solicitacao = Banco.buscarSolicitacaoPorId(Integer.valueOf(id));
            if (solicitacao != null) {
                return Response.ok().entity(solicitacao).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("{ \"mensagem\" : \"Solicitação não encontrada!\" }").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{ \"mensagem\" : \"Falha para obter solicitação!\", \"detalhe\" : \"" + e.getMessage() + "\" }").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response incluirSolicitacao(Solicitacao solicitacao) {
        try {
            if (solicitacao.getId() != null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{ \"mensagem\" : \"A solicitação já possui um ID!\" }").build();
            }
            Solicitacao sol = Banco.incluirSolicitacao(solicitacao);
            return Response.ok().entity(sol).build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{ \"mensagem\" : \"Falha na inclusao da solicitacao!\" , \"detalhe\" :  \""+ e.getMessage() +"\"  }").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterarSolicitacao(@PathParam("id") String id, Solicitacao solicitacao) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"mensagem\" : \"O id da solicitação não pode ser nulo.\"}").build();
        }
        try {
            if (Banco.buscarSolicitacaoPorId(Integer.valueOf(id)) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensagem\" : \"Solicitação não encontrada.\"}").build();
            }
            Solicitacao s = Banco.alterarSolicitacao(Integer.valueOf(id), solicitacao);
            return Response.ok().entity(s).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensagem\" : \"Falha na alteração da solicitação.\", \"detalhe\" : \"" + e.getMessage() + "\"}").build();
        }
    }


    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response excluirSolicitacao(@PathParam("id") String id) throws Exception {
        try {
            if ( Banco.buscarSolicitacaoPorId(Integer.valueOf(id)) == null ) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("{ \"mensagem\" : \"Empregado nao encontrado!\" }").build();
            }
            Banco.excluirSolicitacao(Integer.valueOf(id));
            return Response.ok().entity("{ \"mensagem\" : \"Empregado "+ id + " excluido!\" }").build();
        }catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity("{ \"mensagem\" : \"Falha na exclusao do empregado!\" , \"detalhe\" :  \""+ e.getMessage() +"\"  }").build();
        }
    }

}
