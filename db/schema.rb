# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20170903171106) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "concursos", force: :cascade do |t|
    t.string "name"
    t.string "image"
    t.string "url"
    t.date "startDate"
    t.date "endDate"
    t.text "description"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.bigint "user_id"
    t.string "image2_file_name"
    t.string "image2_content_type"
    t.integer "image2_file_size"
    t.datetime "image2_updated_at"
    t.index ["user_id"], name: "index_concursos_on_user_id"
  end

  create_table "users", force: :cascade do |t|
    t.string "email", default: "", null: false
    t.string "encrypted_password", default: "", null: false
    t.string "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer "sign_in_count", default: 0, null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.inet "current_sign_in_ip"
    t.inet "last_sign_in_ip"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string "first_name"
    t.string "last_name"
    t.index ["email"], name: "index_users_on_email", unique: true
    t.index ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true
  end

  create_table "videos", force: :cascade do |t|
    t.string "name"
    t.text "message"
    t.string "firstNameUser"
    t.string "lastNameUser"
    t.string "emailUser"
    t.boolean "state", default: false
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.bigint "concurso_id"
    t.string "sin_convertir_file_name"
    t.string "sin_convertir_content_type"
    t.integer "sin_convertir_file_size"
    t.datetime "sin_convertir_updated_at"
    t.index ["concurso_id"], name: "index_videos_on_concurso_id"
  end

  add_foreign_key "concursos", "users"
  add_foreign_key "videos", "concursos"
end
