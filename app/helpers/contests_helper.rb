require 'securerandom'
module ContestsHelper
  include ApplicationHelper

  def url_video_original(video)
    root_url_content + 'videos/original/' + video.fileName
  end

  def url_video_convertido(video)
    if video.fileNameConv.nil?
      '/system/videos/convertido/' + 'Error'
    else
      root_url_content + 'videos/convertido/' + video.fileNameConv
    end
  end

  def generate_video_uuid
    SecureRandom.uuid
  end

  def get_id_concurso(url_concurso)
    Rails.cache.fetch(url_concurso, expires_in: 12.hours) do
      concurso = Concurso.query(index_name: 'ConcursoXUrl',
                     key_condition_expression: 'concurso_url = :h',
                     expression_attribute_values: { ':h' => url_concurso }).first()
      concurso.concurso_id
    end
  end

  def delete_video(video)
    delete_file_s3('videos/original/', video.fileName)
    delete_file_s3('videos/convertido/', video.fileNameConv)
  end

  def upload_video_concurso(uploaded_io)
    upload_file_s3(uploaded_io, 'videos/original/')
  end
end
