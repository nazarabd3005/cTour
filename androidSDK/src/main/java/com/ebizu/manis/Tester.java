package com.ebizu.manis;

import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author kazao
 */
public class Tester {

    private static Tester instance;
    private static final String LATITUDE = "-6.211957";
    private static final String LONGITUDE = "106.819081";

    public void businessBusiness(String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.businessBusiness(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessBusinesses() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("keyword", "");
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessBusinesses(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessBusinessesByInterest(String interest) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("interest", interest);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessBusinessesByInterest(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessCategory() {
        JSONObject info = new JSONObject();
        Worker.businessCategory(Session.getInstance(), new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessDelete(String type, String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("type", type);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.businessDelete(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessEvent(String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.businessEvent(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessEventRsvp(String event, String type) {
        JSONObject info = new JSONObject();
        try {
            info.put("event", event);
            info.put("type", type);
        } catch (JSONException e) {
        }
        Worker.businessEventRsvp(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessEvents() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("keyword", "");
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessEvents(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessFollow(String company, String type) {
        JSONObject info = new JSONObject();
        try {
            info.put("company", company);
            info.put("type", type);
        } catch (JSONException e) {
        }
        Worker.businessFollow(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessFollower(String company) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("company", company);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessFollower(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessOffer(String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.businessOffer(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessOffers() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("keyword", "");
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessOffers(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessReview(String company) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("company", company);
            info.put("review", "Verry good");
        } catch (JSONException e) {
        }
        Worker.businessReview(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessReward(String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.businessReward(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessRewards() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("keyword", "");
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessRewards(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessSave(String type, String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("type", type);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.businessSave(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessSaved(String type) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("type", type);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessSaved(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void businessTimeline() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("company", "");
            info.put("category", "");
            info.put("keyword", "");
            info.put("type", "ALL");
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.businessTimeline(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meAccount() {
        JSONObject info = new JSONObject();
        try {
            info.put("address", "Nglempongsari 295C");
            info.put("zip", "55581");
            info.put("city", "Yogyakarta");
        } catch (JSONException e) {
        }

        Worker.meAccount(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meActivity() {
        JSONObject info = new JSONObject();
        try {
            info.put("company", "109");
            info.put("type", "6");
            info.put("value", "");
        } catch (JSONException e) {
        }

        Worker.meActivity(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meCheckin(String company) {
        JSONObject info = new JSONObject();
        try {
            info.put("company", company);
        } catch (JSONException e) {
        }
        Worker.meCheckin(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meFollower() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.meFollower(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meFollowingBusiness() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.meFollowingBusiness(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meFollowingPeople() {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.meFollowingPeople(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meInterest() {
        Worker.meInterest(Session.getInstance(), null, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meLogin() {
        Worker.meLogin(Session.getInstance(), "agus.marto@gmail.com", "1", false, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void mePoint() {
        Worker.mePoint(Session.getInstance(), new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void mePicture(String picture) {
        JSONObject info = new JSONObject();
        try {
            info.put("picture", picture);
        } catch (JSONException e) {
        }
        Worker.mePicture(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }
    
    public void mePush(String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.mePush(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meQrCode() {
        Worker.meQrCode(Session.getInstance(), new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void meSignUp() {
        Member member = new Member();
        member.setName("Agus Marto Wardoyo");
        member.setEmail("agus.marto@gmail.com");
        member.setBirthdate(new Date(1970, 10, 10));
        member.setMobilePhoneNumber("+62811234567");
        member.setGender(Member.Gender.MALE);
        Worker.meSignUp(Session.getInstance(), member.createJsonObject(), "1", new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void peopleFollow(String member, String type) {
        JSONObject info = new JSONObject();
        try {
            info.put("member", member);
            info.put("type", type);
        } catch (JSONException e) {
        }
        Worker.peopleFollow(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void peopleFollower(String member) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("member", member);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.peopleFollower(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void peopleFollowingBusiness(String member) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("member", member);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.peopleFollowingBusiness(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void peopleFollowingPeople(String member) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("member", member);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.peopleFollowingPeople(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void peoplePeople(String id) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("id", id);
        } catch (JSONException e) {
        }
        Worker.peoplePeople(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public void peoplePeoples(String keyword) {
        JSONObject info = new JSONObject();
        try {
            info.put("lat", LATITUDE);
            info.put("lon", LONGITUDE);
            info.put("keyword", keyword);
            info.put("page", "1");
            info.put("size", "20");
        } catch (JSONException e) {
        }
        Worker.peoplePeoples(Session.getInstance(), info, new Callback() {

            @Override
            public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                System.out.println("Error: ");
                Utility.dump(error);
                System.out.println("Object: ");
                Utility.dump(object);
                System.out.println("Array: ");
                Utility.dump(array);
            }
        });
    }

    public static Tester getInstance() {
        if (instance == null) {
            instance = new Tester();
        }
        return instance;
    }

}
