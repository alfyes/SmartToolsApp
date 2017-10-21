require 'aws-sdk-s3'
require 'aws-sdk-sqs'
module ApplicationHelper
  def upload_file_s3(uploaded_io, folder)
    s3 = Aws::S3::Resource.new
    bucket = s3.bucket('smart-tools-app')
    begin
      file_name = get_new_file_name(uploaded_io.original_filename)
      obj = bucket.object(folder + file_name)
    end while obj.exists?

    obj.put(acl: 'public-read', body: uploaded_io.read)
    file_name
  end

  def delete_file_s3(folder, file_name)
    s3 = Aws::S3::Resource.new
    bucket = s3.bucket('smart-tools-app')
    obj = bucket.object(folder + file_name)
    obj.delete
  end

  def get_new_file_name(original)
    file_name = original

    pos_punto = file_name.rindex('.')
    Random.rand(100_000).to_s.prepend(file_name[0, pos_punto] + '_') + file_name[pos_punto, file_name.length]
  end

  def send_message_sqs(video)
    sqs = Aws::SQS::Client.new
    queue_name = 'smart-tools-app'

    begin
      queue_url = sqs.get_queue_url(queue_name: queue_name).queue_url

      send_message_result = sqs
                            .send_message(queue_url: queue_url,
                                          message_body: 'Convertir video ' +
                                              video.name,
                                          message_attributes:
                                             { file_name:
                                                   { string_value:
                                                         video.fileName,
                                                     data_type: 'String' },
                                               video_id:
                                                   { string_value:
                                                         video.video_id,
                                                     data_type: 'String' },
                                               concurso_id:
                                                   { string_value:
                                                         video.concurso_id,
                                                     data_type:
                                                         'String' },
                                               emailUser:
                                                   { string_value:
                                                         video.emailUser,
                                                     data_type:
                                                         'String' },
                                               name:
                                                   { string_value:
                                                         video.name,
                                                     data_type:
                                                         'String' },
                                               firstNameUser:
                                                   { string_value:
                                                         video.firstNameUser,
                                                     data_type:
                                                         'String' } })
    rescue Aws::SQS::Errors::NonExistentQueue
      puts "A queue named '#{queue_name}' does not exist."
    end
    puts send_message_result.message_id
  end

  def root_url_content
    # Desde S3.
    # 'https://smart-tools-app.s3.us-east-2.amazonaws.com/'

    # Desde CloudFront.
    'https://d1iq2lsf8qakxz.cloudfront.net/'
  end
end
