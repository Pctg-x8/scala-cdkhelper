let GHA =
      https://raw.githubusercontent.com/Pctg-x8/gha-schemas/master/schema.dhall

let ProvidedSteps/actions/checkout =
      https://raw.githubusercontent.com/Pctg-x8/gha-schemas/master/ProvidedSteps/actions/checkout.dhall

let setupSBT =
      GHA.Step::{
      , name = "setup sbt"
      , uses = Some "actions/setup-java@v3"
      , `with` = Some
          ( toMap
              { distribution = GHA.WithParameterType.Text "temurin"
              , java-version = GHA.WithParameterType.Text "17"
              , cache = GHA.WithParameterType.Text "sbt"
              }
          )
      }

let publish = GHA.Step::{ name = "publish package", run = Some "sbt publish" }

in  GHA.Workflow::{
    , name = Some "publish"
    , on =
        GHA.On.Detailed
          GHA.OnDetails::{
          , push = Some GHA.OnPush::{ branches = Some [ "master" ] }
          }
    , jobs = toMap
        { publish = GHA.Job::{
          , runs-on = GHA.RunnerPlatform.ubuntu-latest
          , permissions = Some (toMap { packages = "write" })
          , steps =
            [ ProvidedSteps/actions/checkout.stepv3
                ProvidedSteps/actions/checkout.Params::{=}
            , setupSBT
            , publish
            ]
          }
        }
    }
