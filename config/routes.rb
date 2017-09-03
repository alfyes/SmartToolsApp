Rails.application.routes.draw do
  get 'home/index'

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  devise_for :users, controllers: {
    sessions: 'users/sessions',
    passwords: 'users/passwords',
    registrations: 'users/registrations'
  }
  resources :concursos

  get '/contests/:urlconcurso', to: 'contests#index', constraints: { urlconcurso: /([a-zA-Z]|\d|_|\-)+/}
  get '/contests/:urlconcurso/new', to: 'contests#new', constraints: { urlconcurso: /([a-zA-Z]|\d|_|\-)+/}
  post '/contests/:urlconcurso', to: 'contests#create', constraints: { urlconcurso: /([a-zA-Z]|\d|_|\-)+/}
  root 'home#index'
end
