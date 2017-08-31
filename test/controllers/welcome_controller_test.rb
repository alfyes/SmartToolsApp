require 'test_helper'

class WelcomeControllerTest < ActionDispatch::IntegrationTest
  test "should get new" do
    get welcome_new_url
    assert_response :success
  end

  test "should get create" do
    get welcome_create_url
    assert_response :success
  end

  test "should get destroy" do
    get welcome_destroy_url
    assert_response :success
  end

end
