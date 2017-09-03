class ContestsController < ApplicationController
  skip_before_action :authenticate_user!, only: [:index]

  def index
    custom_url_concurso = params[:urlconcurso]
    @concurso = Concurso.find_by_url(custom_url_concurso)
  end
end
